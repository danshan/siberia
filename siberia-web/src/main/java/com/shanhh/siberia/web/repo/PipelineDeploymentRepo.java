package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.PipelineDeployment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author shanhonghao
 * @since 2018-06-14 10:41
 */
public interface PipelineDeploymentRepo extends PagingAndSortingRepository<PipelineDeployment, Integer> {

    Page<PipelineDeployment> findByPipelineId(int pipelineId, Pageable pageable);

}
