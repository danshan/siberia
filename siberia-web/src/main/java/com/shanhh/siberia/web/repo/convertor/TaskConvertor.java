package com.shanhh.siberia.web.repo.convertor;

import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.web.repo.entity.Task;
import com.shanhh.siberia.web.repo.entity.TaskStep;
import org.springframework.beans.BeanUtils;

/**
 * @author shanhonghao
 * @since 2018-05-30 17:26
 */
public class TaskConvertor {

    public static TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(task, dto);
        dto.setEnv(EnvConvertor.toDTO(task.getEnv()));
        dto.setDeployment(PipelineConvertor.toDTO(task.getDeployment()));
        return dto;
    }

    public static Task toPO(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        task.setEnv(EnvConvertor.toPO(taskDTO.getEnv()));
        task.setDeployment(PipelineConvertor.toPO(taskDTO.getDeployment()));
        return task;
    }

    public static TaskStepDTO toDTO(TaskStep step) {
        if (step == null) {
            return null;
        }
        TaskStepDTO dto = new TaskStepDTO();
        BeanUtils.copyProperties(step, dto);
        return dto;
    }

    public static TaskStep toPO(TaskStepDTO dto) {
        if (dto == null) {
            return null;
        }
        TaskStep po = new TaskStep();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    public static TaskDTO.Memo toDTO(Task.Memo memo) {
        if (memo == null) {
            return null;
        }
        TaskDTO.Memo dto = new TaskDTO.Memo();
        BeanUtils.copyProperties(memo, dto);
        return dto;
    }

    public static Task.Memo toPO(TaskDTO.Memo memo) {
        if (memo == null) {
            return null;
        }
        Task.Memo po = new Task.Memo();
        BeanUtils.copyProperties(memo, po);
        return po;
    }

}
