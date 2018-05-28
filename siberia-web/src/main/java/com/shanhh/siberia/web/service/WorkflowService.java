package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.task.TaskDTO;

/**
 * @author shanhonghao
 * @since 2018-05-28 17:25
 */
public interface WorkflowService {
    void startTaskWorkflow(TaskDTO task);
}
