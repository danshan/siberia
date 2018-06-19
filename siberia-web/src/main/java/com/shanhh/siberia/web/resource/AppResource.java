package com.shanhh.siberia.web.resource;

import com.codahale.metrics.annotation.Timed;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppLockDTO;
import com.shanhh.siberia.web.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * @author shanhonghao
 * @since 2018-05-30 14:21
 */
@RestController
@Api(value = "api for deployment apps", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping("api/apps")
@Slf4j
public class AppResource {

    private static final int LIMIT_MAX = 50;
    private static final String LIMIT_DEFAULT = "10";

    @Resource
    private AppService appService;

    @Timed
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "paginate apps", response = BaseResponse.class)
    public BaseResponse paginateApps(
            @ApiParam(value = "start page index", required = false, defaultValue = "0")
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize
    ) {
        Page<AppDTO> pageInfo = appService.paginateApps(
                Math.max(pageNum, 0), Math.min(pageSize, LIMIT_MAX));
        return new BaseResponse(pageInfo);
    }

    @Timed
    @RequestMapping(value = "{appId}", method = RequestMethod.GET)
    @ApiOperation(value = "load app by id", response = BaseResponse.class)
    public BaseResponse loadAppById(
            @Valid @Min(1) @PathVariable("appId") int appId
    ) {
        return new BaseResponse<>(appService.loadAppById(appId).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "locks", method = RequestMethod.GET)
    @ApiOperation(value = "paginate app locks", response = BaseResponse.class)
    public BaseResponse paginateLocks(
            @ApiParam(value = "start page index", required = false, defaultValue = "0")
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize
    ) {
        Page<AppLockDTO> pageInfo = appService.paginateAppLocks(
                Math.max(pageNum, 0), Math.min(pageSize, LIMIT_MAX));
        return new BaseResponse(pageInfo);
    }

    @Timed
    @RequestMapping(value = "{appId}/configs", method = RequestMethod.GET)
    @ApiOperation(value = "find app configs", response = BaseResponse.class)
    public BaseResponse findConfigsByAppId(
            @PathVariable("appId") int appId
    ) {
        return new BaseResponse(appService.findConfigsByAppId(appId));
    }

    @Timed
    @RequestMapping(value = "{appId}/configs/{configId}", method = RequestMethod.GET)
    @ApiOperation(value = "load app config by id", response = BaseResponse.class)
    public BaseResponse loadConfigById(
            @PathVariable("appId") int appId,
            @PathVariable("configId") int configId
    ) {
        return new BaseResponse(appService.loadConfigById(appId, configId));
    }

}
