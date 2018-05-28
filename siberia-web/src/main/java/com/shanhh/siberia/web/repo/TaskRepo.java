package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-28 16:11
 */
public interface TaskRepo extends PagingAndSortingRepository<Task, Integer> {

    List<Task> findByStatus(String status);

}
