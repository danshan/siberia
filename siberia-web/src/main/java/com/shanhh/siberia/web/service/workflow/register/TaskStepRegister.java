package com.shanhh.siberia.web.service.workflow.register;


import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.web.service.workflow.WorkflowBuilder;

/**
 * @author Dan
 * @since 2016-07-29 10:39
 */
public interface TaskStepRegister {

    /**
     * 注册上线发布流程
     */
    void registerDeploySteps(WorkflowBuilder builder, TaskDTO task);


    /**
     * 注册回滚流程
     *
     * @param rollbackBuildNo 回滚的版本号
     */
    void registerRollbackSteps(WorkflowBuilder builder, TaskDTO task, int rollbackBuildNo);
}
