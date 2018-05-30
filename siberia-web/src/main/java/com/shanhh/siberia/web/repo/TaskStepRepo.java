package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.TaskStep;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author shanhonghao
 * @since 2018-05-30 15:43
 */
public interface TaskStepRepo extends PagingAndSortingRepository<TaskStep, Integer> {
}
