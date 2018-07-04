package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-05-22 11:28
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class PipelineDeploymentCreateReq implements Serializable {

    private int pipelineId;
    private int buildNo;
    private String project;
    private String module;

    private String createBy;

}
