package com.shanhh.siberia.client.dto.app;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-29 14:53
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class AppDTO implements Serializable {

    private int id;
    private String project;
    private String module;
    private AppType appType;

    private boolean deleted;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public String getName() {
        return StringUtils.isBlank(this.module)
                ? this.project
                : String.format("%s.%s", StringUtils.trimToEmpty(this.project), StringUtils.trimToEmpty(this.module));
    }
}
