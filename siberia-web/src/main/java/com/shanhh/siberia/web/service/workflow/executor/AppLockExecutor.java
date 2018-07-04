package com.shanhh.siberia.web.service.workflow.executor;


import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.client.dto.task.TaskStepResult;
import com.shanhh.siberia.client.dto.workflow.StepExecutor;
import com.shanhh.siberia.client.dto.workflow.WorkflowDTO;
import com.shanhh.siberia.core.SpringContextHolder;
import com.shanhh.siberia.web.resource.errors.InternalServerErrorException;
import com.shanhh.siberia.web.service.AppService;
import com.shanhh.siberia.web.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Dan
 * @since 2016-06-08 14:45
 */
@Slf4j
@AllArgsConstructor
public class AppLockExecutor implements StepExecutor {

    private final LockStatus lockStatus;

    private final String step = "update_env_lock";
    private final TaskService taskService;
    private final AppService appService;

    public AppLockExecutor(LockStatus lockStatus) {
        this.lockStatus = lockStatus;

        this.taskService = SpringContextHolder.getBean(TaskService.class);
        this.appService = SpringContextHolder.getBean(AppService.class);
    }


    @Override
    public String getDesc() {
        return "update env lock to " + lockStatus;
    }

    @Override
    public void exec(WorkflowDTO workflow) {
        TaskDTO task = workflow.getTask();
        PipelineDeploymentDTO deployment = workflow.getDeployment();
        AppDTO app = deployment.getApp();

        appService.updateLockStatus(app, task.getEnv(), lockStatus, task.getUpdateBy())
                .orElseThrow(() -> new IllegalStateException(String.format("update lock status failed: %s, %s", deployment.getBuildNo(), task.getEnv())));
    }

    @Override
    public void onSuccess(WorkflowDTO workflow) {
        TaskDTO task = workflow.getTask();
        log.info("update app lock status: {}, {}, {}", task.getId(), task.getEnv(), lockStatus);
        String detail = "update task env lock status for deploy: " + lockStatus;
        TaskStepDTO taskStep = taskService.createTaskStep(
                task.getId(),
                step,
                TaskStepResult.OK,
                detail,
                task.getUpdateBy())
                .orElseThrow(() -> new InternalServerErrorException(String.format("create step failed, taskId=%s, step=%s, detail=%s", task.getId(), step, detail)));
    }

    @Override
    public void onFailed(WorkflowDTO workflow, Throwable throwable) throws Throwable {
        TaskDTO task = workflow.getTask();
        log.error("lock app failed, " + workflow.getTask(), throwable);
        String detail = StringUtils.substring(String.format("want to update task env lock to %s, but failed. cause: %s", lockStatus, throwable.getMessage()), 0, DETAIL_LENGTH);
        TaskStepDTO taskStep = taskService.createTaskStep(
                task.getId(),
                step,
                TaskStepResult.ERROR,
                detail,
                task.getUpdateBy())
                .orElseThrow(() -> new InternalServerErrorException(String.format("create step failed, taskId=%s, step=%s, detail=%s", task.getId(), step, detail)));
        throw throwable;
    }
}
