package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.PipelineTask;
import org.springframework.data.repository.CrudRepository;

/**
 * @author shanhonghao
 * @since 2018-06-19 15:11
 */
public interface PipelineTaskRepo extends CrudRepository<PipelineTask, Integer> {
}
