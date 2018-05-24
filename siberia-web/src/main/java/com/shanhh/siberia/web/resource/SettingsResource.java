package com.shanhh.siberia.web.resource;

import com.codahale.metrics.annotation.Timed;
import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.service.SettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Timed
    @RequestMapping(value = "envs", method = RequestMethod.GET)
    @ApiOperation(value = "find environments", response = BaseResponse.class)
    public BaseResponse<List<EnvDTO>> findEnvs() {
        List<EnvDTO> envs = settingsService.findEnvs();
        return new BaseResponse(envs);
    }

    @Timed
    @RequestMapping(value = "envs/{envId}", method = RequestMethod.GET)
    @ApiOperation(value = "paginate pipelines", response = BaseResponse.class)
    public BaseResponse<EnvDTO> deleteEnvById(
            @ApiParam(value = "environment id", required = true)
            @PathVariable("envId") int envId
    ) {
        return new BaseResponse(settingsService.deleteEnvById(envId).orElseThrow(ResourceNotFoundException::new));
    }
}
