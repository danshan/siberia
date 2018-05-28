package com.shanhh.siberia.web.service.impl;

import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.web.repo.TaskRepo;
import com.shanhh.siberia.web.resource.TaskResource;
import com.shanhh.siberia.web.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-23 10:39
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepo taskRepo;

    @Override
    public Page<TaskDTO> paginateTasks(int pageNum, int pageSize) {
        List<TaskDTO> results = Lists.newLinkedList();
        for (int i = 0; i < pageSize; i++) {
            results.add(TaskDTO.mock());
        }
        return new PageImpl(results);
    }

    @Override
    public List<TaskDTO> findTasksByStatus(TaskStatus status) {
        taskRepo.findByStatus(status.value);
        return Lists.newArrayList();
    }
}
