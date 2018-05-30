package com.shanhh.siberia.web.service.workflow;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.workflow.StepExecutor;
import com.shanhh.siberia.client.dto.workflow.WorkflowDTO;

import java.util.List;

/**
 * @author Dan
 * @since 2016-06-08 12:30
 */
public class WorkflowBuilder {

    private List<StepExecutor> stepChain;
    private List<StepExecutor> failedChain;
    private TaskDTO task;

    private WorkflowBuilder() {
    }

    public static WorkflowBuilder getInstance() {
        return new WorkflowBuilder();
    }

    public WorkflowBuilder withTask(TaskDTO task) {
        Preconditions.checkNotNull(task, "task should not be null.");
        this.task = task;
        return this;
    }

    public WorkflowBuilder register(StepExecutor executor) {
        Preconditions.checkNotNull(executor, "executor should not be null.");
        if (this.stepChain == null) {
            this.stepChain = Lists.newLinkedList();
        }
        this.stepChain.add(executor);
        return this;
    }

    public WorkflowBuilder registerFailed(StepExecutor executor) {
        Preconditions.checkNotNull(executor, "executor should not be null.");
        if (this.failedChain == null) {
            this.failedChain = Lists.newLinkedList();
        }
        this.failedChain.add(executor);
        return this;
    }

    public WorkflowDTO build() {
        return new WorkflowDTO(task, stepChain, failedChain);
    }

}
