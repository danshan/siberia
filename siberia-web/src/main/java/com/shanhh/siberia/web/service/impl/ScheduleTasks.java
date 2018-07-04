package com.shanhh.siberia.web.service.impl;

import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.web.service.TaskService;
import com.shanhh.siberia.web.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-28 16:02
 */
@Component
@Slf4j
public class ScheduleTasks {

    @Resource
    private TaskService taskService;
    @Resource
    private WorkflowService workflowService;

    @Scheduled(cron = "0/3 * * * * ? ")   //每3秒执行一次
    public void checkCreatedTasks() {
        List<TaskDTO> tasks = taskService.findTasksByStatus(TaskStatus.CREATED);

        // 循环队列, 并执行上线操作
        tasks.stream().forEach(this::startWorkflow);
    }

    private void startWorkflow(TaskDTO task) {
        taskService.updateTaskStatusById(task, TaskStatus.SERVICING)
                .ifPresent(targetTask -> {
                    try {
                        log.info("start task: {}", task.getId());
                        workflowService.startTaskWorkflow(task);
                        log.info("finish task: {}", task.getId());
                    } catch (Exception e) {
                        log.error("task interrupted", e);
                        taskService.updateTaskStatusById(targetTask, TaskStatus.FAIL);
                    }
                });
    }
}
