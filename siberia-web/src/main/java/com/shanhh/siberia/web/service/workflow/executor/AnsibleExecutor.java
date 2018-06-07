package com.shanhh.siberia.web.service.workflow.executor;


import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStepDTO;
import com.shanhh.siberia.client.dto.task.TaskStepResult;
import com.shanhh.siberia.client.dto.workflow.StepExecutor;
import com.shanhh.siberia.client.dto.workflow.WorkflowDTO;
import com.shanhh.siberia.core.SpringContextHolder;
import com.shanhh.siberia.web.resource.errors.InternalServerErrorException;
import com.shanhh.siberia.web.service.AnsibleService;
import com.shanhh.siberia.web.service.TaskStepService;
import com.shanhh.siberia.web.service.ansible.AnsibleResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Dan
 * @since 2016-06-20 10:52
 */
@AllArgsConstructor
@Slf4j
public class AnsibleExecutor implements StepExecutor {
    private final String inventory;
    private final String ymlFile;
    private final Map<String, Object> extVars;

    private final String step = "execute_ansible";

    @Override
    public String getDesc() {
        return "ansible playbook exec: " + ymlFile;
    }

    @Override
    public void exec(WorkflowDTO workflow) throws Exception {
        AnsibleResult ansibleResult = SpringContextHolder.getBean(AnsibleService.class)
                .execPlaybook(workflow.getTask(), inventory, ymlFile, extVars);
        if (ansibleResult.getExitCode() != 0) {
            throw ansibleResult.getExecuteException();
        }
    }

    @Override
    public void onSuccess(WorkflowDTO workflow) {
        log.info("ansible success: " + workflow.getTask());
        TaskDTO task = workflow.getTask();
        String detail = "execute ansible success " + ymlFile;
        TaskStepDTO taskStep = SpringContextHolder.getBean(TaskStepService.class)
                .createStep(
                        task.getId(),
                        step,
                        TaskStepResult.OK,
                        detail,
                        task.getUpdateBy())
                .orElseThrow(() -> new InternalServerErrorException(String.format("create step failed, taskId=%s, step=%s, detail=%s", task.getId(), step, detail)));
    }

    @Override
    public void onFailed(WorkflowDTO workflow, Throwable throwable) throws Throwable {
        log.error(String.format("ansible failed: %s. %s", throwable.getMessage(), workflow.getTask()));
        TaskDTO task = workflow.getTask();
        String detail = StringUtils.substring(String.format("want to execute ansible %s, failed: %s", ymlFile, throwable.getMessage()), 0, DETAIL_LENGTH);
        TaskStepDTO taskStep = SpringContextHolder.getBean(TaskStepService.class).createStep(
                task.getId(),
                step,
                TaskStepResult.ERROR,
                detail,
                task.getUpdateBy())
                .orElseThrow(() -> new InternalServerErrorException(String.format("create step failed, taskId=%s, step=%s, detail=%s", task.getId(), step, detail)));
        throw throwable;
    }

}
