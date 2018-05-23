package com.shanhh.siberia.web.service;

import com.github.pagehelper.PageInfo;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;

/**
 * @author shanhonghao
 * @since 2018-05-18 15:58
 */
public interface PipelineService {
    PageInfo<PipelineDTO> paginatePipelines(int pageNum, int pageSize);

    PageInfo<PipelineDeploymentDTO> paginatePipelineDeployments(int pageNum, int pageSize);

    PipelineDTO loadPipeline(String pipelineId);
}
