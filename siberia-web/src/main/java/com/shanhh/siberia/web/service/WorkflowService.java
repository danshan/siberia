package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskRedeployReq;
import com.shanhh.siberia.client.dto.task.TaskRollbackReq;

import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-28 17:25
 */
public interface WorkflowService {
    Optional<TaskDTO> startTaskWorkflow(TaskDTO task);

    Optional<TaskDTO> rollbackTaskById(TaskRollbackReq task);

    Optional<TaskDTO> redeployTaskById(TaskRedeployReq task);
}
