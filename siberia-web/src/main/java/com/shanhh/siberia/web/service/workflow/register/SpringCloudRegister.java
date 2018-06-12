package com.shanhh.siberia.web.service.workflow.register;


import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppHostDTO;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.core.SpringContextHolder;
import com.shanhh.siberia.web.resource.errors.InternalServerErrorException;
import com.shanhh.siberia.web.service.AppService;
import com.shanhh.siberia.web.service.TaskService;
import com.shanhh.siberia.web.service.workflow.WorkflowBuilder;
import com.shanhh.siberia.web.service.workflow.executor.AnsibleExecutor;
import com.shanhh.siberia.web.service.workflow.executor.TaskNodeUpdateExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Dan
 * @since 2016-07-29 10:56
 */
@Slf4j(topic = "ansible")
public class SpringCloudRegister implements TaskStepRegister {

    @Override
    public void registerDeploySteps(WorkflowBuilder builder, TaskDTO task) {
        AppService sibAppService = SpringContextHolder.getBean(AppService.class);
        AppDTO app = sibAppService.loadAppByModule(task.getProject(), task.getModule())
                .orElseThrow(() -> new InternalServerErrorException(String.format("app not supported, project: %s, module: %s", task.getProject(), task.getModule())));

        List<String> toDeployHosts = Lists.newLinkedList();
        try {
            AppHostDTO appHost = sibAppService.loadAppHostByEnv(task.getProject(), task.getModule(), task.getEnv()).orElse(new AppHostDTO());
            List<String> hosts = (appHost == null || appHost.getHosts() == null) ? Lists.newArrayList() : appHost.getHosts();
            toDeployHosts.addAll(hosts);
            log.info("affect hosts: {}", Joiner.on(",").join(toDeployHosts));
            if (toDeployHosts.size() == 0) {
                return;
            }
        } catch (Exception e) {
            log.error(String.format("fetch node list failed, %s", task.toString()), e);
        }

        builder.register(new TaskNodeUpdateExecutor(toDeployHosts));
        registerSteps(builder, task.getEnv(), task, app, toDeployHosts, task.getBuildNo());
    }

    @Override
    public void registerRollbackSteps(WorkflowBuilder builder, TaskDTO task, int rollbackBuildNo) {
        log.info("rollback to {}, task={}", rollbackBuildNo, task);

        AppService appService = SpringContextHolder.getBean(AppService.class);
        AppDTO app = appService.loadAppByModule(task.getProject(), task.getModule())
                .orElseThrow(() -> new InternalServerErrorException(String.format("app not supported, project: %s, module: %s", task.getProject(), task.getModule())));

        TaskService taskService = SpringContextHolder.getBean(TaskService.class);
        TaskDTO.Memo memo = task.getMemo();
        memo.setRollbackVersion(rollbackBuildNo);
        taskService.updateTaskStatusAndMemoById(task.getId(), TaskStatus.RUNNING, memo);

        registerSteps(builder, task.getEnv(), task, app, task.getNodes(), rollbackBuildNo);
    }

    private void registerSteps(WorkflowBuilder builder, EnvDTO env, TaskDTO task, AppDTO app, List<String> toDeployHosts, int buildNo) {
        try {
            AppDTO.Config config = app.getConfigByEnv(env);
            builder.register(new AnsibleExecutor("inventory",
                    "_app_springcloud_nodes_update.yml",
                    ImmutableMap.<String, Object>builder()
                            .put("buildno", buildNo)
                            .put("hosts", Joiner.on(",").join(toDeployHosts))
                            .put("app", buildAppName(task))
                            .put("app_service", generateService(task))
                            .put("project", task.getProject())
                            .put("module", task.getModule())
                            .put("port", config.getConfigs().getOrDefault("SERVER_PORT", 8080))
                            .build()));
        } catch (Exception e) {
            throw new InternalServerErrorException(String.format("register spring cloud failed, %s, %s", task, e.getMessage()));
        }
    }

    private String buildAppName(TaskDTO task) {
        return StringUtils.defaultIfBlank(task.getModule(), task.getProject());
    }

    private String generateService(TaskDTO task) throws Exception {
        String appName = buildAppName(task);

        AppDTO app = SpringContextHolder.getBean(AppService.class)
                .loadAppByModule(task.getProject(), task.getModule())
                .orElseThrow(() -> new InternalServerErrorException(String.format("app not supported, project: %s, module: %s", task.getProject(), task.getModule())));
        Map<String, Object> configs = app.getConfigByEnv(task.getEnv()).getConfigs();

        PropertiesConfiguration defaultConfig = new PropertiesConfiguration(Resources.getResource("spring-boot.boot.properties"));
        defaultConfig.addProperty("APP_NAME", appName);
        defaultConfig.addProperty("SERVER_PORT", String.valueOf(configs.getOrDefault("SERVER_PORT", 8080)));
        defaultConfig.addProperty("CLASSPATH_USER", StringUtils.trimToEmpty((String) configs.get("CLASSPATH_USER")));
        defaultConfig.addProperty("JAVA_OPTS_USER", StringUtils.trimToEmpty((String) configs.get("JAVA_OPTS_USER")));
        configs.entrySet().forEach(entry -> defaultConfig.addProperty(entry.getKey(), entry.getValue()));

        String cmd = defaultConfig.getString("CMD");
        Map<String, String> values = ImmutableMap.<String, String>builder()
                .put("APP_NAME", appName)
                .put("CONFIG_FOLDER", defaultConfig.getString("CONFIG_FOLDER"))
                .put("CMD", cmd)
                .build();
        String file = Resources.toString(Resources.getResource("spring-boot.template.service"), Charsets.UTF_8);
        String service = new StringSubstitutor(values).replace(file);
        File tempFile = File.createTempFile(appName, ".service");
        Files.write(service, tempFile, Charsets.UTF_8);
        log.info("write service to {}", tempFile.getAbsoluteFile());
        return tempFile.getAbsolutePath();
    }

}
