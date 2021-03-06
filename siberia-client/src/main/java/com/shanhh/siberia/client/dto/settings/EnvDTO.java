package com.shanhh.siberia.client.dto.settings;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-22 11:33
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class EnvDTO implements Serializable {

    private int id;
    private String name;
    private String description;

    private boolean deleted;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
}
