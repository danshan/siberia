package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-06-20 09:51
 */
@Data
@NoArgsConstructor
public class PipelineStatusUpdateReq implements Serializable {
    @NotNull
    private PipelineStatus status;
}
