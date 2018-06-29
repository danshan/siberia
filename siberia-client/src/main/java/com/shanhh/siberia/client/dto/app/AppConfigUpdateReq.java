package com.shanhh.siberia.client.dto.app;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @author shanhonghao
 * @since 2018-06-07 17:43
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class AppConfigUpdateReq implements Serializable {
    @Min(1)
    private int appId;
    @Min(1)
    private int envId;
    @NotNull
    private Map<String, Object> content = Maps.newHashMap();
    private String operator;
}
