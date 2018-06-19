package com.shanhh.siberia.web.resource;

import com.codahale.metrics.annotation.Timed;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.service.SettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-24 16:11
 */
@RestController
@Api(value = "api for settings", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping(value = "api/settings", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class SettingsResource {

    @Resource
    private SettingsService settingsService;

    private static final int LIMIT_MAX = 50;
    private static final String LIMIT_DEFAULT = "10";

    @Timed
    @RequestMapping(value = "envs", method = RequestMethod.GET)
    @ApiOperation(value = "find environments", response = BaseResponse.class)
    public BaseResponse paginateEnvs(
            @ApiParam(value = "start page index", required = false, defaultValue = "0")
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,

            @ApiParam(value = "page size", required = false, defaultValue = LIMIT_DEFAULT)
            @RequestParam(value = "pageSize", required = false, defaultValue = LIMIT_DEFAULT) int pageSize
    ) {
        Page<EnvDTO> pageInfo = settingsService.paginateEnvs(
                Math.max(pageNum, 0), Math.min(pageSize, LIMIT_MAX));
        return new BaseResponse(pageInfo);
    }

    @Timed
    @RequestMapping(value = "envs/{envId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "delete environment by id", response = BaseResponse.class)
    public BaseResponse<EnvDTO> deleteEnvById(
            @ApiParam(value = "environment id", required = true)
            @PathVariable("envId") int envId
    ) {
        return new BaseResponse(settingsService.deleteEnvById(envId).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "envs/{envId}", method = RequestMethod.PUT)
    @ApiOperation(value = "update environment by id", response = BaseResponse.class)
    public BaseResponse<EnvDTO> updateEnvById(
            @ApiParam(value = "environment id", required = true)
            @PathVariable("envId") int envId,

            @RequestBody EnvDTO env
    ) {
        env.setId(envId);
        return new BaseResponse(settingsService.updateEnvById(env).orElseThrow(ResourceNotFoundException::new));
    }

    @Timed
    @RequestMapping(value = "envs", method = RequestMethod.POST)
    @ApiOperation(value = "create environment", response = BaseResponse.class)
    public BaseResponse<EnvDTO> createEnv(
            @RequestBody EnvDTO env
    ) {
        return new BaseResponse(settingsService.createEnv(env).orElseThrow(ResourceNotFoundException::new));
    }

}
