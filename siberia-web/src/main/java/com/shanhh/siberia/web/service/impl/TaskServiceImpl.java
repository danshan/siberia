package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
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
import org.springframework.data.domain.PageRequest;
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
    public Optional<TaskDTO> createTask(TaskCreateReq taskReq) {
        Task task = new Task();
        task.setDeploymentId(taskReq.getDeploymentId());
        task.setEnv(envRepo.findOne(taskReq.getEnvId()));
        task.setStatus(TaskStatus.CREATED);
        task.setCreateBy(taskReq.getCreateBy());
        task.setUpdateBy(taskReq.getCreateBy());

        Task result = taskRepo.save(task);
        log.info("task created, {}", result);
        return Optional.ofNullable(TaskConvertor.toDTO(result));

    }

    @Override
    public Page<TaskDTO> paginateTasks(int pageNum, int pageSize) {
        Page<TaskDTO> page = taskRepo.findAll(new PageRequest(pageNum, pageSize))
                .map(TaskConvertor::toDTO);
        return page;
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
            log.info("task status changed, {}", task);
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

        TaskStep taskStep = new TaskStep();
        taskStep.setTaskId(taskId);
        taskStep.setStep(step);
        taskStep.setResult(result);
        taskStep.setDetail(detail);
        taskStep.setCreateBy(operator);
        taskStep.setUpdateBy(operator);

        TaskStep saved = taskStepRepo.save(taskStep);
        log.info("task step created, {}", saved);
        return Optional.ofNullable(TaskConvertor.toDTO(saved));
    }

    @Override
    public int startTaskById(int taskId, TaskStatus taskStatus) {
        int result = taskRepo.updateTaskStatusForStartById(taskId, taskStatus);
        log.info("task status updated to {}, taskId={}", taskStatus, taskId);
        return result;
    }

    @Override
    public int endTaskById(int taskId, TaskStatus taskStatus) {
        int result = taskRepo.updateTaskStatusForEndById(taskId, taskStatus);
        log.info("task status updated to {}, taskId={}", taskStatus, taskId);
        return result;
    }

    @Override
    public int updateTaskNodesById(int taskId, List<String> nodes) {
        int result = taskRepo.updateTaskNodesById(taskId, Joiner.on(",").join(nodes));
        log.info("task nodes updated to {}, taskId={}", nodes, taskId);
        return result;
    }

    @Override
    public int updateTaskStatusAndMemoById(int taskId, TaskStatus status, TaskDTO.Memo memo) {
        int result = taskRepo.updateTaskStatusAndMemoById(taskId, status, TaskConvertor.toPO(memo));
        log.info("task status updated to {}, taskId={}", status, taskId);
        return result;
    }

}
