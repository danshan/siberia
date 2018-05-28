package com.shanhh.siberia.web.service.impl;

import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.web.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    @Scheduled(cron = "0/3 * * * * ? ")   //每3秒执行一次
    public void checkCreatedTasks() {
        List<TaskDTO> tasks = taskService.findTasksByStatus(TaskStatus.CREATED);
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        // 循环队列, 并执行上线操作
//        parseTasks(tasks);
    }
}
