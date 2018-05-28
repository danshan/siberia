package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-23 10:39
 */
public interface TaskService {

    Page<TaskDTO> paginateTasks(int pageNum, int pageSize);

    List<TaskDTO> findTasksByStatus(TaskStatus status);

    Optional<TaskDTO> updateTaskStatusById(TaskDTO taskDTO, TaskStatus targetStatus);
}
