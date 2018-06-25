package com.shanhh.siberia.client.dto.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-06-25 17:07
 */
@Data
@NoArgsConstructor
public class EnvUpdateReq implements Serializable {
    @Min(1)
    private int id;
    @Size(min = 1, max = 128)
    private String name;
    @NotNull
    @Size(min = 0, max = 255)
    private String description;
    private String updateBy;
    private Date updateTime;
}
