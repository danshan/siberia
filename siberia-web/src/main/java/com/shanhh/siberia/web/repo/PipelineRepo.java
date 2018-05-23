package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.Pipeline;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author shanhonghao
 * @since 2018-05-23 11:32
 */
public interface PipelineRepo extends PagingAndSortingRepository<Pipeline, Integer> {
}
