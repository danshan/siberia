package com.shanhh.siberia.client.dto.pipeline;

import com.shanhh.siberia.client.dto.app.AppDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-22 11:28
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class PipelineDeploymentDTO implements Serializable {

    private int id;
    private int pipelineId;
    private int buildNo;
    private AppDTO app;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}
