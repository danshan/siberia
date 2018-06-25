package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.TaskStep;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-30 15:43
 */
public interface TaskStepRepo extends PagingAndSortingRepository<TaskStep, Integer> {

    List<TaskStep> findByTaskId(int taskId);
}
