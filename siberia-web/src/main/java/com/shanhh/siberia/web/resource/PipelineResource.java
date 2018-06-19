package com.shanhh.siberia.web.resource;

import com.codahale.metrics.annotation.Timed;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineTaskDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineTaskReq;
import com.shanhh.siberia.web.service.PipelineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * @author shanhonghao
 * @since 2018-05-17 15:42
 */
@RestController
@Api(value = "api for deployment pipelines", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping("api/pipelines")
@Slf4j
public class PipelineResource {

    @Resource
    private PipelineService pipelineService;

    private static final int LIMIT_MAX = 50;
    private static final String LIMIT_DEFAULT = "10";

    @Timed
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "paginate pipelines", response = BaseResponse.class)
    public BaseResponse paginatePipelines(
            @ApiParam(value = "start page index", required = false, defaultValue = "0")
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize
    ) {
        Page<PipelineDTO> pageInfo = pipelineService.paginatePipelines(
                Math.max(pageNum, 0), Math.min(pageSize, LIMIT_MAX));
        return new BaseResponse(pageInfo);
    }

    @Timed
    @RequestMapping(value = "{pipelineId}", method = RequestMethod.GET)
    @ApiOperation(value = "paginate pipelines", response = BaseResponse.class)
    public BaseResponse<PipelineDTO> loadPipeline(
            @ApiParam(value = "pipeline id", required = true)
            @PathVariable("pipelineId") int pipelineId
    ) {
        return new BaseResponse<>(pipelineService.loadPipeline(pipelineId).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "{pipelineId}/deployments", method = RequestMethod.GET)
    @ApiOperation(value = "paginate pipeline deployments for pipeline id", response = BaseResponse.class)
    public BaseResponse paginatePipelineDeployments(
            @ApiParam(value = "start page index", required = false, defaultValue = "0")
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,

            @ApiParam(value = "pipeline id", required = true)
            @Valid @Min(1) @PathVariable("pipelineId") int pipelineId
    ) {
        Page<PipelineDeploymentDTO> pageInfo = pipelineService.paginatePipelineDeployments(
                Math.max(pageNum, 0), Math.min(pageSize, LIMIT_MAX), pipelineId);
        return new BaseResponse(pageInfo);
    }

    @Timed
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create pipeline", response = BaseResponse.class)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<PipelineDTO> createPipeline(
            @Valid @RequestBody PipelineDTO pipeline
    ) {
        return new BaseResponse<>(pipelineService.createPipeline(pipeline.getTitle(), pipeline.getDescription(), null).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "{pipelineId}/deployments", method = RequestMethod.POST)
    @ApiOperation(value = "create pipeline deployment", response = BaseResponse.class)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<PipelineDeploymentDTO> createPipelineDeployment(
            @Valid @Min(1) @PathVariable int pipelineId,
            @Valid @RequestBody PipelineDeploymentDTO deployment
    ) {
        return new BaseResponse<>(pipelineService.createPipelineDeployment(
                pipelineId, deployment.getProject(), deployment.getModule(), deployment.getBuildNo(), null).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "{pipelineId}/tasks", method = RequestMethod.POST)
    @ApiOperation(value = "create pipeline task", response = BaseResponse.class)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<PipelineTaskDTO> createPipelineTask(
            @Valid @Min(1) @PathVariable int pipelineId,
            @Valid @RequestBody PipelineTaskReq task
    ) {
        task.setCreateBy("demo");
        return new BaseResponse<>(pipelineService.createPipelineTask(task)
                .orElseThrow(ResourceNotFoundException::new));
    }

}
