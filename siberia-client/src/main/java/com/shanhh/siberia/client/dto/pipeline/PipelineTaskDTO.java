package com.shanhh.siberia.client.dto.pipeline;

import com.shanhh.siberia.client.dto.settings.EnvDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-22 11:30
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class PipelineTaskDTO implements Serializable {
    private int id;
    private PipelineDeploymentDTO deployment;
    private EnvDTO env;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
}
