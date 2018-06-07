package com.shanhh.siberia.web.service.workflow.register;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
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

import java.util.List;

/**
 * @author dan
 * @since 2017-05-18 09:14
 */
@Slf4j(topic = "ansible")
public class NodeJsRegister implements TaskStepRegister {

    @Override
    public void registerDeploySteps(WorkflowBuilder builder, TaskDTO task) {
        AppService appService = SpringContextHolder.getBean(AppService.class);
        AppDTO app = appService.loadAppByModule(task.getProject(), task.getModule())
                .orElseThrow(() -> new InternalServerErrorException(String.format("app not supported, project: %s, module: %s", task.getProject(), task.getModule())));

        List<String> toDeployHosts = Lists.newLinkedList();
        try {
            AppHostDTO sibAppHostDTO = appService.loadAppHostByEnv(task.getProject(), task.getModule(), task.getEnv()).orElse(new AppHostDTO());
            List<String> hosts = (sibAppHostDTO == null || sibAppHostDTO.getHosts() == null) ? Lists.newArrayList() : sibAppHostDTO.getHosts();
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

        TaskService sibTaskService = SpringContextHolder.getBean(TaskService.class);
        TaskDTO.Memo memo = task.getMemo();
        memo.setRollbackVersion(rollbackBuildNo);
        sibTaskService.updateTaskStatusAndMemoById(task.getId(), TaskStatus.RUNNING, memo);

        registerSteps(builder, task.getEnv(), task, app, task.getNodes(), rollbackBuildNo);

    }

    private void registerSteps(WorkflowBuilder builder, EnvDTO env, TaskDTO task, AppDTO app, List<String> hosts, int buildNo) {
        AppDTO.Config config = app.getConfigByEnv(env);
        builder.register(new AnsibleExecutor("inventory",
                "_app_nodejs_nodes_update.yml",
                ImmutableMap.<String, Object>builder()
                        .put("buildno", buildNo)
                        .put("hosts", Joiner.on(",").join(hosts))
                        .put("app", task.getModule())
                        .put("project", task.getProject())
                        .put("module", task.getModule())
                        .put("port", config.getConfigs().getOrDefault("SERVER_PORT", 80))
                        .build()));
    }

}
