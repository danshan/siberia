package com.shanhh.siberia.web.service.impl;

import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.client.dto.workflow.StepExecutor;
import com.shanhh.siberia.client.dto.workflow.WorkflowDTO;
import com.shanhh.siberia.web.resource.errors.CustomParameterizedException;
import com.shanhh.siberia.web.service.AppService;
import com.shanhh.siberia.web.service.WorkflowService;
import com.shanhh.siberia.web.service.workflow.TaskStepRegisterFactory;
import com.shanhh.siberia.web.service.workflow.WorkflowBuilder;
import com.shanhh.siberia.web.service.workflow.executor.AppLockExecutor;
import com.shanhh.siberia.web.service.workflow.executor.TaskEndExecutor;
import com.shanhh.siberia.web.service.workflow.executor.TaskStartExecutor;
import com.shanhh.siberia.web.service.workflow.register.TaskStepRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-28 17:28
 */
@Service
@Slf4j
public class WorkflowServiceImpl implements WorkflowService {

    @Resource
    private AppService appService;

    @Override
    @Transactional
    public void startTaskWorkflow(TaskDTO task) {
        List<String> relatedUsers = Lists.newArrayList(task.getCreateBy());

        AppDTO app = appService.loadAppByModule(task.getProject(), task.getModule())
                .orElseThrow(() -> new CustomParameterizedException(String.format("app not supported, project: %s, module: %s", task.getProject(), task.getModule())));

        WorkflowBuilder workflowBuilder = WorkflowBuilder.getInstance();

        workflowBuilder.withTask(task)
                // 修改task 状态为 running
                .register(new TaskStartExecutor(TaskStatus.RUNNING))
                // 锁定task环境
                .register(new AppLockExecutor(LockStatus.LOCKED));

        TaskStepRegisterFactory factory = new TaskStepRegisterFactory();
        TaskStepRegister register = factory.getRegister(app.getAppType());
        register.registerDeploySteps(workflowBuilder, task);

        workflowBuilder
                .register(new TaskEndExecutor(TaskStatus.OK))
//                // 解锁task环境
                .register(new AppLockExecutor(LockStatus.UNLOCKED))
//                // 通知相关人员发布完成
//                .register(new WechatMsgExecutor(context, relatedUsers, buildWxMsgTitle("发布完成", task), buildWxMsgContent(task), sibTaskStepService))
//                // 通知相关人员发布失败
                .registerFailed(new TaskEndExecutor(TaskStatus.FAIL));
//                .registerFailed(new WechatMsgExecutor(context, relatedUsers,
//                        buildWxMsgTitle("发布失败", task),
//                        buildWxMsgContent(task), sibTaskStepService));
        WorkflowDTO workflow = workflowBuilder.build();

        try {
            startWorkflowStep(workflow, workflow.getStepChain());
        } catch (Throwable throwable) {
            try {
                startWorkflowStep(workflow, workflow.getFailedChain());
            } catch (Throwable t) {
                log.error("", t);
            }
        }
    }

    private void startWorkflowStep(WorkflowDTO workflow, List<StepExecutor> chain) throws Throwable {
        if (CollectionUtils.isEmpty(chain)) {
            return;
        }
        for (StepExecutor executor : chain) {
            try {
                executor.exec(workflow);
                executor.onSuccess(workflow);
            } catch (Throwable e) {
                executor.onFailed(workflow, e);
            }
        }
    }
}
