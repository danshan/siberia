package com.shanhh.siberia.web.service.workflow.executor;


import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.client.dto.task.TaskStepResult;
import com.shanhh.siberia.client.dto.workflow.StepExecutor;
import com.shanhh.siberia.client.dto.workflow.WorkflowDTO;
import com.shanhh.siberia.core.SpringContextHolder;
import com.shanhh.siberia.web.resource.errors.InternalServerErrorException;
import com.shanhh.siberia.web.service.TaskService;
import com.shanhh.siberia.web.service.TaskStepService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 更新task中已经发布的nodes节点信息
 *
 * @author Dan
 * @since 2016-06-08 14:45
 */
@Slf4j
@AllArgsConstructor
public class TaskNodeUpdateExecutor implements StepExecutor {

    private final List<String> nodes;
    private final String step = "update_task_nodes";

    @Override
    public String getDesc() {
        return "update deployed nodes to " + Joiner.on(",").join(nodes);
    }

    @Override
    public void exec(WorkflowDTO workflow) {
        TaskService taskService = SpringContextHolder.getBean(TaskService.class);
        TaskDTO task = workflow.getTask();

        Preconditions.checkState(taskService.updateTaskNodesById(task.getId(), nodes) > 0,
                "update task nodes failed: %s, %s" + task.getId(), task.getEnv());
        task.setNodes(nodes);
    }

    @Override
    public void onSuccess(WorkflowDTO workflow) {
        TaskDTO task = workflow.getTask();
        log.info("update task nodes: {}, {}, {}", task.getId(), task.getEnv(), nodes);
        String detail = "update nodes to " + nodes;
        TaskStepDTO taskStep = SpringContextHolder.getBean(TaskStepService.class).createStep(
                task.getId(),
                step,
                TaskStepResult.OK,
                detail,
                task.getUpdateBy())
                .orElseThrow(() -> new InternalServerErrorException(String.format("create step failed, taskId=%s, step=%s, detail=%s", task.getId(), step, detail)));
    }

    @Override
    public void onFailed(WorkflowDTO workflow, Throwable throwable) throws Throwable {
        log.error("update task nodes failed, " + workflow.getTask(), throwable);
        TaskDTO task = workflow.getTask();
        String detail = StringUtils.substring(String.format("want to update task nodes to %s, but failed. cause: %s", nodes, throwable.getMessage()), 0, DETAIL_LENGTH);
        TaskStepDTO taskStep = SpringContextHolder.getBean(TaskStepService.class).createStep(
                task.getId(),
                step,
                TaskStepResult.ERROR,
                detail,
                task.getUpdateBy())
                .orElseThrow(() -> new InternalServerErrorException(String.format("create step failed, taskId=%s, step=%s, detail=%s", task.getId(), step, detail)));
        throw throwable;
    }
}
