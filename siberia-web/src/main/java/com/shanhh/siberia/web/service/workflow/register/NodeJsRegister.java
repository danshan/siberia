package com.shanhh.siberia.web.service.workflow.register;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.app.AppConfigDTO;
import com.shanhh.siberia.client.dto.app.AppHostDTO;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.core.SpringContextHolder;
import com.shanhh.siberia.web.resource.errors.SiberiaException;
import com.shanhh.siberia.web.service.AppService;
import com.shanhh.siberia.web.service.TaskService;
import com.shanhh.siberia.web.service.workflow.WorkflowBuilder;
import com.shanhh.siberia.web.service.workflow.executor.AnsibleExecutor;
import com.shanhh.siberia.web.service.workflow.executor.TaskNodeUpdateExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

        List<String> toDeployHosts = Lists.newLinkedList();
        try {
            AppHostDTO sibAppHostDTO = appService.loadAppHostByAppAndEnv(task.getAppId(), task.getEnv().getId()).orElse(new AppHostDTO());
            List<String> hosts = (sibAppHostDTO == null || sibAppHostDTO.getHosts() == null) ? Lists.newArrayList() : sibAppHostDTO.getHosts();
            toDeployHosts.addAll(hosts);
            log.info("task {}, affect hosts: {}", task.getId(), Joiner.on(",").join(toDeployHosts));
            if (toDeployHosts.size() == 0) {
                return;
            }
        } catch (Exception e) {
            log.error(String.format("task {} fetch node list failed, %s", task.getId(), task.toString()), e);
        }

        builder.register(new TaskNodeUpdateExecutor(toDeployHosts));

        registerSteps(builder, task, toDeployHosts, task.getBuildNo());
    }

    @Override
    public void registerRollbackSteps(WorkflowBuilder builder, TaskDTO task, int rollbackBuildNo) {
        log.info("task {} rollback to {}, task={}", task.getId(), rollbackBuildNo, task);

        TaskService sibTaskService = SpringContextHolder.getBean(TaskService.class);
        TaskDTO.Memo memo = task.getMemo();
        memo.setRollbackVersion(rollbackBuildNo);
        sibTaskService.updateTaskStatusAndMemoById(task.getId(), TaskStatus.RUNNING, memo);

        registerSteps(builder, task, task.getNodes(), rollbackBuildNo);
    }

    private void registerSteps(WorkflowBuilder builder, TaskDTO task, List<String> toDeployHosts, int buildNo) {
        AppService appService = SpringContextHolder.getBean(AppService.class);
        AppConfigDTO config = appService.loadConfigByEnv(task.getAppId(), task.getEnv().getId())
                .orElseThrow(() -> new IllegalArgumentException("app config not found"));
        builder.register(new AnsibleExecutor("inventory",
                "_app_nodejs_nodes_update.yml",
                ImmutableMap.<String, Object>builder()
                        .put("buildno", buildNo)
                        .put("hosts", Joiner.on(",").join(toDeployHosts))
                        .put("app", buildAppName(task))
                        .put("project", task.getProject())
                        .put("module", task.getModule())
                        .put("port", config.getContent().getOrDefault("SERVER_PORT", 80))
                        .build()));
    }

    private String buildAppName(TaskDTO task) {
        return StringUtils.defaultIfBlank(task.getModule(), task.getProject());
    }
}
