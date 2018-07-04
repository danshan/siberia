package com.shanhh.siberia.client.dto.pipeline;

import com.shanhh.siberia.client.dto.task.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-07-04 14:43
 */
@Data
@NoArgsConstructor
public class PipelineDeploymentProcessDTO implements Serializable {
    private int deploymentId;
    private int envId;
    private String envName;
    private int taskId;
    private TaskStatus status;
}
