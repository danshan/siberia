package com.shanhh.siberia.web.resource;

import com.codahale.metrics.annotation.Timed;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.task.TaskCreateReq;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskRedeployReq;
import com.shanhh.siberia.client.dto.task.TaskRollbackReq;
import com.shanhh.siberia.web.resource.errors.SiberiaException;
import com.shanhh.siberia.web.service.TaskService;
import com.shanhh.siberia.web.service.WorkflowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author shanhonghao
 * @since 2018-05-23 10:35
 */
@RestController
@Api(value = "api for deployment tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping(value = "api/tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class TaskResource {

    @Resource
    private TaskService taskService;
    @Resource
    private WorkflowService workflowService;

    private static final int LIMIT_MAX = 50;
    private static final String LIMIT_DEFAULT = "10";

    @Timed
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create task", response = BaseResponse.class)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<TaskDTO> createTask(
            @RequestBody TaskCreateReq task
    ) {
        task.setCreateBy("sys");
        return new BaseResponse<>(taskService.createTask(task)
                .orElseThrow(() -> new SiberiaException("create task failed")));
    }

    @Timed
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "paginate tasks", response = BaseResponse.class)
    public BaseResponse paginateTasks(
            @ApiParam(value = "start page index", required = false, defaultValue = "0")
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize
    ) {
        Page<TaskDTO> pageInfo = taskService.paginateTasks(
                Math.max(pageNum, 0), Math.min(pageSize, LIMIT_MAX));
        return new BaseResponse(pageInfo);
    }

    @Timed
    @RequestMapping(value = "rollback", method = RequestMethod.POST)
    @ApiOperation(value = "rollback task", response = BaseResponse.class)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<TaskDTO> rollbackTask(
            @RequestBody TaskRollbackReq task
    ) {
        task.setCreateBy("sys");
        return new BaseResponse<>(workflowService.rollbackTaskById(task)
                .orElseThrow(() -> new SiberiaException("rollback task failed")));
    }

    @Timed
    @RequestMapping(value = "redeploy", method = RequestMethod.POST)
    @ApiOperation(value = "redeploy task", response = BaseResponse.class)
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<TaskDTO> redeployTask(
            @RequestBody TaskRedeployReq task
    ) {
        task.setCreateBy("sys");
        return new BaseResponse<>(workflowService.redeployTaskById(task)
                .orElseThrow(() -> new SiberiaException("redeploy task failed")));
    }

}
