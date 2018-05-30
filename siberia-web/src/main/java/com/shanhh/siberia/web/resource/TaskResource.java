package com.shanhh.siberia.web.resource;

import com.codahale.metrics.annotation.Timed;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.task.TaskCreateRequest;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.web.resource.errors.InternalServerErrorException;
import com.shanhh.siberia.web.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    private static final int LIMIT_MAX = 50;
    private static final String LIMIT_DEFAULT = "10";

    @Timed
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create task", response = BaseResponse.class)
    public BaseResponse<TaskDTO> createTask(
            @RequestBody TaskCreateRequest task
    ) {
        task.setCreateBy("sys");
        return new BaseResponse<>(taskService.createTask(task)
                .orElseThrow(() -> new InternalServerErrorException("create task failed")));
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

}
