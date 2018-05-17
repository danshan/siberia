package com.shanhh.siberia.client.dto.pipeline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author dan
 * @since 2017-01-24 10:35
 */
@Data
@NoArgsConstructor
public class CreatePipelineReq implements Serializable {

    @ApiModelProperty("title of pipeline")
    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @ApiModelProperty("description of pipeline")
    @NotNull
    @Size(min = 1)
    private String desc;

}
