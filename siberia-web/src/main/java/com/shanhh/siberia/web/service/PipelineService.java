package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-18 15:58
 */
public interface PipelineService {
    Page<PipelineDTO> paginatePipelines(int pageNum, int pageSize);

    Page<PipelineDeploymentDTO> paginatePipelineDeployments(int pageNum, int pageSize, int pipelineId);

    Optional<PipelineDTO> loadPipeline(int pipelineId);

    PipelineDTO createPipeline(String title, String description, String createBy);

    PipelineDeploymentDTO createPipelineDeployment(int pipelineId, String project, String module, int buildNo, String createBy);
}
