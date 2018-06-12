package com.shanhh.siberia.client.dto.app;

import com.shanhh.siberia.client.dto.settings.EnvDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-06-07 17:43
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class AppConfigDTO implements Serializable {
    private int id;
    private EnvDTO env;
    private String createBy;
    private String updateBy;

    private Date createTime;
    private Date updateTime;
}
