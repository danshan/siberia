package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.task.TaskDTO;
import org.springframework.data.domain.Page;

/**
 * @author shanhonghao
 * @since 2018-05-23 10:39
 */
public interface TaskService {

    Page<TaskDTO> paginateTasks(int pageNum, int pageSize);
}
