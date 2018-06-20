package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.client.dto.pipeline.PipelineStatus;
import com.shanhh.siberia.web.repo.entity.Pipeline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-23 11:32
 */
public interface PipelineRepo extends PagingAndSortingRepository<Pipeline, Integer> {

    @Query("from Pipeline p where p.status in (:status)")
    Page<Pipeline> findByStatus(@Param("status") List<PipelineStatus> status, Pageable pageable);

}
