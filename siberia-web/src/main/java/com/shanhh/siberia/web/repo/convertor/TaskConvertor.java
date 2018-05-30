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
        return dto;
    }

    public static Task toPO(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }
        Task task = new Task();
        BeanUtils.copyProperties(taskDTO, task);
        task.setEnv(EnvConvertor.toPO(taskDTO.getEnv()));
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

}
