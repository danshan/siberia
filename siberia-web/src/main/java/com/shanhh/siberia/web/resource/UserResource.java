package com.shanhh.siberia.web.resource;

import com.shanhh.siberia.client.base.BaseResponse;
import com.shanhh.siberia.client.dto.User.CurrentUserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shanhonghao
 * @since 2018-05-23 11:18
 */
@RestController
@Api(value = "api for deployment pipelines", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping("api/users")
@Slf4j
public class UserResource {

    @RequestMapping(value = "currentUser", method = RequestMethod.GET)
    @ApiOperation(value = "load current user", response = BaseResponse.class)
    public BaseResponse<CurrentUserDTO> loadCurrentUser() {
        return new BaseResponse<>(CurrentUserDTO.mock());
    }
}
