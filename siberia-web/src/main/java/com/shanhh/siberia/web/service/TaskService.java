package com.shanhh.siberia.web.service;

import com.github.pagehelper.PageInfo;
import com.shanhh.siberia.client.dto.task.TaskDTO;

/**
 * @author shanhonghao
 * @since 2018-05-23 10:39
 */
public interface TaskService {

    PageInfo<TaskDTO> paginateTasks(int pageNum, int pageSize);
}
