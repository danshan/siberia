package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.pipeline.*;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-18 15:58
 */
public interface PipelineService {

    // pipeline

    Page<PipelineDTO> paginatePipelines(int pageNum, int pageSize, PipelineStatus status);

    Optional<PipelineDTO> loadPipeline(int pipelineId);

    Optional<PipelineDTO> createPipeline(String title, String description, String createBy);

    Optional<PipelineDTO> updatePipelineById(PipelineUpdateReq request);

    Optional<PipelineDTO> updatePipelineStatusById(PipelineStatusUpdateReq request);

    // deployment

    Page<PipelineDeploymentDTO> paginatePipelineDeployments(int pageNum, int pageSize, int pipelineId);

    Optional<PipelineDeploymentDTO> loadPipelineDeploymentById(int deploymentId);

    Optional<PipelineDeploymentDTO> createPipelineDeployment(int pipelineId, String project, String module, int buildNo, String createBy);

    // task

    Optional<PipelineTaskDTO> createPipelineTask(PipelineTaskReq task);

}
