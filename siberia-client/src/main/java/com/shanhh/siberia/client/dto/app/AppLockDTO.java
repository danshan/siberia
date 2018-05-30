package com.shanhh.siberia.client.dto.app;


import com.shanhh.siberia.client.dto.settings.EnvDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dan
 * @since 2016-06-03 02:52
 */
@Data
@NoArgsConstructor
public class AppLockDTO implements Serializable {

    private static final long serialVersionUID = -585792658917389060L;

    private int id;
    private String project;
    private String module;
    private EnvDTO env;
    private LockStatus lockStatus;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public String getApp() {
        if (StringUtils.isNoneBlank(module)) {
            return project + "." + module;
        }
        return project;
    }

}
