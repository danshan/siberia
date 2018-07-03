package com.shanhh.siberia.client.dto.app;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-06-07 17:43
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class AppHostUpdateReq implements Serializable {
    @Min(1)
    private int appId;
    @Min(1)
    private int envId;
    @NotNull
    private List<String> hosts;
    private String operator;
}
