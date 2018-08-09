package com.shanhh.siberia.web.resource;

import com.codahale.metrics.annotation.Timed;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.pipeline.*;
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
import java.util.List;

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
    @GetMapping
    @ApiOperation(value = "paginate pipelines", response = BaseResponse.class)
    public BaseResponse paginatePipelines(
            @ApiParam(value = "start page index", required = false, defaultValue = "0")
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize,

            @ApiParam(value = "pipepline status", required = false, defaultValue = "0")
            @RequestParam(value = "status", required = false, defaultValue = "0") int status,

            @ApiParam(value = "create by", required = false, defaultValue = "")
            @RequestParam(value = "createBy", required = false, defaultValue = "0") String createBy
    ) {
        Page<PipelineDTO> pageInfo = pipelineService.paginatePipelines(
                Math.max(pageNum, 0), Math.min(pageSize, LIMIT_MAX), PipelineStatus.of(status));
        return new BaseResponse(pageInfo);
    }

    @Timed
    @GetMapping(value = "{pipelineId}")
    @ApiOperation(value = "paginate pipelines", response = BaseResponse.class)
    public BaseResponse<PipelineDTO> loadPipeline(
            @ApiParam(value = "pipeline id", required = true)
            @PathVariable("pipelineId") int pipelineId
    ) {
        return new BaseResponse<>(pipelineService.loadPipeline(pipelineId).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "{pipelineId}", method = RequestMethod.PUT)
    @ApiOperation(value = "update pipeline by id", response = BaseResponse.class)
    public BaseResponse<PipelineDTO> updatePipelineById(
            @ApiParam(value = "pipeline id", required = true)
            @PathVariable("pipelineId") int pipelineId,

            @Valid @RequestBody PipelineUpdateReq request
    ) {
        request.setPipelineId(pipelineId);
        request.setUpdateBy("sys");
        return new BaseResponse<>(pipelineService.updatePipelineById(request).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "{pipelineId}/status", method = RequestMethod.PUT)
    @ApiOperation(value = "update pipeline status", response = BaseResponse.class)
    public BaseResponse<PipelineDTO> updatePipelineStatusById(
            @ApiParam(value = "pipeline id", required = true)
            @PathVariable("pipelineId") int pipelineId,

            @Valid @RequestBody PipelineStatusUpdateReq request
    ) {
        request.setPipelineId(pipelineId);
        request.setUpdateBy("sys");
        return new BaseResponse<>(pipelineService.updatePipelineStatusById(request).orElseThrow(ResourceNotFoundException::new));
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
            @Valid @RequestBody PipelineDeploymentCreateReq request
    ) {
        return new BaseResponse<>(pipelineService.createPipelineDeployment(request)
                .orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "{pipelineId}/deployments/{deploymentId}/processes", method = RequestMethod.GET)
    @ApiOperation(value = "create pipeline deployment", response = BaseResponse.class)
    public BaseResponse<List<PipelineDeploymentProcessDTO>> findPipelineDeploymentProcesses(
            @Valid @Min(1) @PathVariable int deploymentId
    ) {
        return new BaseResponse<>(pipelineService.findDeploymentProcess(deploymentId));
    }
}
