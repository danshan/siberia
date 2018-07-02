package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-06-20 09:51
 */
@Data
@NoArgsConstructor
@ToString
public class PipelineUpdateReq implements Serializable {
    private int pipelineId;
    @NotNull
    @Size(min = 1, max = 64)
    private String title;
    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    private String updateBy;
}
