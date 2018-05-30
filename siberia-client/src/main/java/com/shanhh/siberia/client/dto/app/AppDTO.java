package com.shanhh.siberia.client.dto.app;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-29 14:53
 */
@Data
@NoArgsConstructor
public class AppDTO implements Serializable {

    private int id;
    private String project;
    private String module;
    private AppType appType;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}
