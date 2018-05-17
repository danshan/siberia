package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 简化的发布流程的信息, 用于列表页显示
 *
 * @author Dan
 * @since 2016-03-15 13:43
 */
@Data
@NoArgsConstructor
public class SibPipelineSimpleDTO implements Serializable {

    private int id;
    private String title;
    private String createBy;
    /**
     * 相关项目
     */
    private List<String> tags;
    private Date createTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
