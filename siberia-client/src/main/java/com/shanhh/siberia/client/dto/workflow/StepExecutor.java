package com.shanhh.siberia.client.dto.workflow;

/**
 * @author Dan
 * @since 2016-06-08 12:23
 */
public interface StepExecutor {

    int DETAIL_LENGTH = 500;

    String getDesc();

    void exec(WorkflowDTO workflow) throws Throwable;

    void onSuccess(WorkflowDTO workflow);

    void onFailed(WorkflowDTO workflow, Throwable throwable) throws Throwable;

}
