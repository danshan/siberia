package com.shanhh.siberia.client.dto.workflow;


import com.shanhh.siberia.client.dto.task.TaskDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


/**
 * @author Dan
 * @since 2016-06-08 11:43
 */
@AllArgsConstructor
public class WorkflowDTO {

    @Getter
    private final TaskDTO task;
    @Getter
    private final List<StepExecutor> stepChain;
    @Getter
    private final List<StepExecutor> failedChain;

}
