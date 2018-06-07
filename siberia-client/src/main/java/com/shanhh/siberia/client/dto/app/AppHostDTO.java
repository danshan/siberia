package com.shanhh.siberia.client.dto.app;

import com.shanhh.siberia.client.dto.settings.EnvDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AppHostDTO implements Serializable {

    private int id;
    private String project;
    private String module;
    private EnvDTO env;
    private List<String> hosts;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
}
