package com.shanhh.siberia.web.service.impl;


import com.google.common.base.Preconditions;
import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.client.dto.task.TaskStepResult;
import com.shanhh.siberia.web.repo.TaskStepRepo;
import com.shanhh.siberia.web.repo.convertor.TaskConvertor;
import com.shanhh.siberia.web.repo.entity.TaskStep;
import com.shanhh.siberia.web.service.TaskStepService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Dan
 * @since 2016-06-23 15:45
 */
@Service
@Slf4j
public class TaskStepServiceImpl implements TaskStepService {

    @Resource
    private TaskStepRepo taskStepRepo;


    @Override
    public List<TaskStepDTO> findStepsByTaskId(int taskId) {
        Preconditions.checkArgument(taskId > 0, "task id should be positive number.");
        return taskStepRepo.findByTaskId(taskId).stream().map(TaskConvertor::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<TaskStepDTO> createStep(int taskId, String step, TaskStepResult result, String detail, String operator) {
        Preconditions.checkArgument(taskId > 0, "task id should be positive number");
        Preconditions.checkArgument(StringUtils.isNotBlank(step), "step should not be blank");
        Preconditions.checkNotNull(result, "result should not be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(detail), "detail should not be blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(operator), "operator should not be blank");

        TaskStepDTO taskStep = buildTaskStep(taskId, step, result, detail, operator);
        TaskStep saved = taskStepRepo.save(TaskConvertor.toPO(taskStep));
        return Optional.ofNullable(TaskConvertor.toDTO(saved));
    }

    private TaskStepDTO buildTaskStep(int taskId, String step, TaskStepResult result, String detail, String operator) {
        TaskStepDTO stepDTO = new TaskStepDTO();
        stepDTO.setTaskId(taskId);
        stepDTO.setStep(StringUtils.trimToEmpty(step));
        stepDTO.setDetail(StringUtils.trimToEmpty(detail));
        stepDTO.setResult(result);
        stepDTO.setCreateBy(operator.trim());
        stepDTO.setUpdateBy(operator.trim());
        return stepDTO;
    }
}
