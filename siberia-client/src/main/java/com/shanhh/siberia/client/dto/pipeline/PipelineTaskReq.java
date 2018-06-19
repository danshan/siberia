package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-06-19 15:41
 */
@Data
@NoArgsConstructor
public class PipelineTaskReq implements Serializable {
    private int deploymentId;
    private int envId;
    private String createBy;
}
