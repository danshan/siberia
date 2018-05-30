package com.shanhh.siberia.client.dto.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dan
 * @since 2016-06-04 15:06
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class TaskStepDTO implements Serializable {

    private static final long serialVersionUID = -3277249735837083741L;

    private int id;
    private int taskId;
    private String step;
    private String detail;
    private TaskStepResult result;
    private String createBy;
    private String updateBy;
    private Date beginTime;
    private Date endTime;
    private Date createTime;
    private Date updateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
