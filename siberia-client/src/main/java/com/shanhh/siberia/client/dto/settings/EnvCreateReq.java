package com.shanhh.siberia.client.dto.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-06-25 17:07
 */
@Data
@NoArgsConstructor
public class EnvCreateReq implements Serializable {
    @Size(min = 1, max = 128)
    private String name;
    @NotNull
    @Size(min = 0, max = 255)
    private String description;
    private String createBy = "sys";
    private String updateBy = "sys";
}
