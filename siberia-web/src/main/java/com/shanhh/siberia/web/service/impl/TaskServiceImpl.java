package com.shanhh.siberia.web.service.impl;

import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.web.repo.TaskRepo;
import com.shanhh.siberia.web.repo.entity.Task;
import com.shanhh.siberia.web.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<TaskDTO> updateTaskStatusById(TaskDTO taskDTO, TaskStatus targetStatus) {
        Task task = convert(taskDTO);
        task.setStatus(targetStatus.value);
        try {
            Task result = taskRepo.save(task);
            return Optional.ofNullable(convert(result));
        } catch (StaleObjectStateException exception) {
            log.info("task status has been changed, {}", task);
            return Optional.empty();
        }
    }

    private TaskDTO convert(Task task) {
        if (task == null) {
            return null;
        }
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);
        return dto;
    }

    private Task convert(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }
        Task po = new Task();
        BeanUtils.copyProperties(taskDTO, po);
        return po;
    }
}
