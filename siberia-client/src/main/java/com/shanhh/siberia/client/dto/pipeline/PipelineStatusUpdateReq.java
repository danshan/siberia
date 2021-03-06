package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-06-20 09:51
 */
@Data
@NoArgsConstructor
@ToString
public class PipelineStatusUpdateReq implements Serializable {
    private int pipelineId;
    @NotNull
    private PipelineStatus status;
    private String updateBy;
}
