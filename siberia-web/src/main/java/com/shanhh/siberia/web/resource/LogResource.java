package com.shanhh.siberia.web.resource;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.web.service.TaskStepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-06-28 09:55
 */
@RestController
@Api(value = "api for logger", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping("api/logs")
@Slf4j
public class LogResource {

    @Resource
    private TaskStepService taskStepService;

    @RequestMapping(value = "siberialog/{taskId}", method = RequestMethod.GET)
    @ApiOperation(value = "load logs of siberia by task id", response = BaseResponse.class)
    public BaseResponse siberialog(
            @ApiParam(value = "task id", required = true)
            @PathVariable(value = "taskId", required = true) int taskId
    ) {
        Preconditions.checkArgument(taskId > 0, "task id should be positive number.");

        try {
            List<TaskStepDTO> steps = taskStepService.findStepsByTaskId(taskId);
            return new BaseResponse(steps);
        } catch (Exception e) {
            log.error("read siberia log failed: {}, {}", taskId, e.getMessage());
            return new BaseResponse(Lists.newArrayList(e.getMessage()));
        }
    }
}
