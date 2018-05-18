package com.shanhh.siberia.client.dto.pipeline;

import com.shanhh.siberia.client.base.RespCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shanhonghao
 * @since 2018-05-18 15:49
 */
@Setter
@Getter
@AllArgsConstructor
public class BaseResponse<T> {

    private RespCode respCode;
    private T data;

    public BaseResponse() {
        this(new RespCode(), null);
    }

    public BaseResponse(String code, String message, T data) {
        this(new RespCode(StringUtils.trimToEmpty(code), StringUtils.trimToEmpty(message)), data);
    }

    public BaseResponse(RespCode respCode) {
        this(respCode, null);
    }

    public BaseResponse(T data) {
        this(new RespCode(), data);
    }

}
