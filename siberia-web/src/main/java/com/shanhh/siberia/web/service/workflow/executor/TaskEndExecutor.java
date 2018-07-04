package com.shanhh.siberia.web.service.workflow.executor;


import com.google.common.base.Preconditions;
import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.client.dto.task.TaskStepResult;
import com.shanhh.siberia.client.dto.workflow.StepExecutor;
import com.shanhh.siberia.client.dto.workflow.WorkflowDTO;
import com.shanhh.siberia.core.SpringContextHolder;
import com.shanhh.siberia.web.resource.errors.InternalServerErrorException;
import com.shanhh.siberia.web.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Dan
 * @since 2016-06-08 14:45
 */
@AllArgsConstructor
@Slf4j
public class TaskEndExecutor implements StepExecutor {

    private final TaskStatus taskStatus;

    private final String step = "end_task";
    private final TaskService taskService;

    public TaskEndExecutor(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;

        this.taskService = SpringContextHolder.getBean(TaskService.class);
    }

    @Override
    public String getDesc() {
        return "update task status to " + taskStatus;
    }

    @Override
    public void exec(WorkflowDTO workflow) {
        TaskDTO task = workflow.getTask();

        Preconditions.checkState(taskService.endTaskById(task.getId(), taskStatus) > 0,
                "update task status failed: %s, %s" + task.getId(), task.getEnv());
        task.setStatus(taskStatus);
    }

    @Override
    public void onSuccess(WorkflowDTO workflow) {
        TaskDTO task = workflow.getTask();
        PipelineDeploymentDTO deployment = workflow.getDeployment();
        AppDTO app = deployment.getApp();

        log.info("update task status: {}, {}, {}, {}, {}",
                new Object[]{app.getProject(), app.getModule(), deployment.getBuildNo(), task.getEnv(), taskStatus});

        String detail = "Job status:" + taskStatus.value;
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
        log.error("update task status failed, " + workflow.getTask(), throwable);
        String detail = StringUtils.substring(String.format("want to update task status to %s, but failed. cause: %s", taskStatus.value, throwable.getMessage()), 0, DETAIL_LENGTH);
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
