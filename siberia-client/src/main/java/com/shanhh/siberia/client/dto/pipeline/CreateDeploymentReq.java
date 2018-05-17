package com.shanhh.siberia.client.dto.pipeline;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author dan
 * @since 2017-01-24 10:35
 */
@Data
@NoArgsConstructor
public class CreateDeploymentReq implements Serializable {

    @ApiModelProperty("project name")
    @NotNull
    @Size(min = 1, max = 255)
    @JsonProperty("proj")
    private String project;

    @ApiModelProperty("module name")
    private String module;

    @ApiModelProperty("description of pipeline")
    @NotNull
    @Min(1)
    @JsonProperty("buildno")
    private int buildNo;

    @ApiModelProperty("deployment id")
    @JsonProperty("deploymentid")
    private int deploymentId;

    @ApiModelProperty("environment")
    @NotNull
    @Size(min = 1, max = 10)
    private String env;

    @ApiModelProperty("percent")
    private int percent = 100;

}
