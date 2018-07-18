package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.client.dto.task.*;
import com.shanhh.siberia.web.config.WebSocketConfiguration;
import com.shanhh.siberia.web.repo.EnvRepo;
import com.shanhh.siberia.web.repo.TaskRepo;
import com.shanhh.siberia.web.repo.TaskStepRepo;
import com.shanhh.siberia.web.repo.convertor.EnvConvertor;
import com.shanhh.siberia.web.repo.convertor.TaskConvertor;
import com.shanhh.siberia.web.repo.entity.Env;
import com.shanhh.siberia.web.repo.entity.Task;
import com.shanhh.siberia.web.repo.entity.TaskStep;
import com.shanhh.siberia.web.service.AppService;
import com.shanhh.siberia.web.service.PipelineService;
import com.shanhh.siberia.web.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @Resource
    private PipelineService pipelineService;
    @Resource
    private AppService appService;

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Optional<TaskDTO> createTask(TaskCreateReq taskReq) {
        PipelineDeploymentDTO deployment = pipelineService.loadPipelineDeploymentById(taskReq.getDeploymentId())
                .orElseThrow(() -> new IllegalArgumentException("deployment not found"));
        appService.loadLockByApp(deployment.getApp().getId(), taskReq.getEnvId())
                .ifPresent(lock -> Preconditions.checkState(lock.getLockStatus() == LockStatus.UNLOCKED, "app is locked"));

        Task task = new Task();
        task.setDeploymentId(deployment.getId());
        task.setBuildNo(deployment.getBuildNo());
        task.setAppId(deployment.getApp().getId());
        task.setProject(deployment.getApp().getProject());
        task.setModule(deployment.getApp().getModule());
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
        Page<TaskDTO> page = taskRepo.findAll(new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id"))
                .map(TaskConvertor::toDTO);
        return page;
    }

    @Override
    public List<TaskDTO> findTaskStatusByDeployemnt(int deploymentId) {
        return taskRepo.findStatusByDeploymentId(deploymentId).stream()
                .map(TaskConvertor::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findTasksByStatus(TaskStatus status) {
        List<Task> tasks = taskRepo.findByStatus(status);
        return tasks.stream().map(TaskConvertor::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<TaskDTO> loadTaskById(int taskId) {
        return Optional.ofNullable(TaskConvertor.toDTO(taskRepo.findOne(taskId)));
    }

    @Override
    public Optional<TaskDTO> updateTaskStatusById(TaskDTO taskDTO, TaskStatus targetStatus) {
        Task task = TaskConvertor.toPO(taskDTO);
        task.setStatus(targetStatus);
        try {
            Task result = taskRepo.save(task);
            log.info("task status changed, {}", task);
            pushTaskUpdatedEvent(taskDTO.getId(), targetStatus);
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
        pushTaskUpdatedEvent(taskId, taskStatus);
        return result;
    }

    @Override
    public int endTaskById(int taskId, TaskStatus taskStatus) {
        int result = taskRepo.updateTaskStatusForStartById(taskId, taskStatus);
        log.info("task status updated to {}, taskId={}", taskStatus, taskId);
        pushTaskUpdatedEvent(taskId, taskStatus);
        return result;
    }



    /**
     * find last success task of same project / module / environment
     *
     * @param currentTask
     * @return
     */
    @Override
    public Optional<TaskDTO> loadLastOkTask(TaskDTO currentTask) {
        Preconditions.checkNotNull(currentTask, "current task not exists");

        Env env = EnvConvertor.toPO(currentTask.getEnv());
        // 上一次的成功的发布
        Task lastTask = taskRepo.findLastTask(currentTask.getId(), currentTask.getProject(), currentTask.getModule(), env, TaskStatus.OK);
        return Optional.ofNullable(TaskConvertor.toDTO(lastTask));
    }

    private void pushTaskUpdatedEvent(int taskId, TaskStatus status) {
        Map<String, Object> msg = ImmutableMap.<String, Object>builder()
                .put("taskId", taskId)
                .put("status", status)
                .put("updateTime", new Date())
                .build();
        simpMessagingTemplate.convertAndSend(WebSocketConfiguration.TASK, msg);
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
