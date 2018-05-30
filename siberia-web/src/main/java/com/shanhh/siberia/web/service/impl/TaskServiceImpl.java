package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.task.*;
import com.shanhh.siberia.web.repo.EnvRepo;
import com.shanhh.siberia.web.repo.TaskRepo;
import com.shanhh.siberia.web.repo.TaskStepRepo;
import com.shanhh.siberia.web.repo.convertor.TaskConvertor;
import com.shanhh.siberia.web.repo.entity.Task;
import com.shanhh.siberia.web.repo.entity.TaskStep;
import com.shanhh.siberia.web.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shanhonghao
 * @since 2018-05-23 10:39
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepo taskRepo;
    @Resource
    private TaskStepRepo taskStepRepo;
    @Resource
    private EnvRepo envRepo;

    @Override
    public Optional<TaskDTO> createTask(TaskCreateRequest taskReq) {
        Task task = new Task();
        task.setPipelineId(taskReq.getPipelineId());
        task.setEnv(envRepo.findOne(taskReq.getEnvId()));
        task.setProject(StringUtils.trimToEmpty(taskReq.getProject()));
        task.setModule(StringUtils.trimToEmpty(taskReq.getModule()));
        task.setBuildNo(taskReq.getBuildNo());
        task.setStatus(TaskStatus.CREATED);
        task.setCreateBy(taskReq.getCreateBy());
        task.setUpdateBy(taskReq.getCreateBy());

        Task result = taskRepo.save(task);
        return Optional.ofNullable(TaskConvertor.toDTO(result));

    }

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
        List<Task> tasks = taskRepo.findByStatus(status);
        return tasks.stream().map(TaskConvertor::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<TaskDTO> updateTaskStatusById(TaskDTO taskDTO, TaskStatus targetStatus) {
        Task task = TaskConvertor.toPO(taskDTO);
        task.setStatus(targetStatus);
        try {
            Task result = taskRepo.save(task);
            return Optional.ofNullable(TaskConvertor.toDTO(result));
        } catch (StaleObjectStateException exception) {
            log.info("task status has been changed, {}", task);
            return Optional.empty();
        }
    }

    @Override
    public List<TaskStepDTO> findTaskStepsByTaskId(int taskId) {
        return null;
    }

    @Override
    public Optional<TaskStepDTO> createTaskStep(int taskId, String step, TaskStepResult result, String detail, String operator) {
        Preconditions.checkArgument(taskId > 0, "task id should be positive number");
        Preconditions.checkArgument(StringUtils.isNotBlank(step), "step should not be blank");
        Preconditions.checkNotNull(result, "result should not be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(detail), "detail should not be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(operator), "operator should not be blank");

        TaskStep taskStep= new TaskStep();
        taskStep.setTaskId(taskId);
        taskStep.setStep(step);
        taskStep.setResult(result);
        taskStep.setDetail(detail);
        taskStep.setCreateBy(operator);
        taskStep.setUpdateBy(operator);

        return Optional.ofNullable(TaskConvertor.toDTO(taskStepRepo.save(taskStep)));
    }

    @Override
    public int startTaskById(int taskId, TaskStatus taskStatus) {
        return taskRepo.updateTaskStatusForStartById(taskId, taskStatus);
    }

    @Override
    public int endTaskById(int taskId, TaskStatus taskStatus) {
        return taskRepo.updateTaskStatusForEndById(taskId, taskStatus);
    }

}
