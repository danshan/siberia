package com.shanhh.siberia.web.service;


import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.client.dto.task.TaskStepResult;

import java.util.List;
import java.util.Optional;

/**
 * @author Dan
 * @since 2016-06-23 15:44
 */
public interface TaskStepService {

    List<TaskStepDTO> findStepsByTaskId(int taskId);

    Optional<TaskStepDTO> createStep(int taskId, String step, TaskStepResult result, String detail, String operator);
}
