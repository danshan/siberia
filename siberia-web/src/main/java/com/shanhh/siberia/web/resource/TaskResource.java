package com.shanhh.siberia.web.resource;

import com.github.pagehelper.PageInfo;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.web.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "paginate tasks", response = BaseResponse.class)
    public BaseResponse paginatePipelines(
            @ApiParam(value = "start page index", required = false, defaultValue = "1")
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize
    ) {
        PageInfo<TaskDTO> pageInfo = taskService.paginateTasks(
                Math.max(pageNum, 1), Math.min(pageSize, LIMIT_MAX));
        return new BaseResponse(pageInfo);
    }

}
