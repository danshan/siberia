package com.shanhh.siberia.web.service;

import com.github.pagehelper.PageInfo;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;

import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-18 15:58
 */
public interface PipelineService {
    PageInfo<PipelineDTO> paginatePipelines(int pageNum, int pageSize);

    PageInfo<PipelineDeploymentDTO> paginatePipelineDeployments(int pageNum, int pageSize);

    Optional<PipelineDTO> loadPipeline(int pipelineId);

    PipelineDTO createPipeline(String title, String description, String createBy);
}
