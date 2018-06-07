package com.shanhh.siberia.web.service.workflow;


import com.shanhh.siberia.client.dto.app.AppType;
import com.shanhh.siberia.web.service.workflow.register.NodeJsRegister;
import com.shanhh.siberia.web.service.workflow.register.SpringCloudRegister;
import com.shanhh.siberia.web.service.workflow.register.TaskStepRegister;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Dan
 * @since 2016-07-29 10:48
 */
@Slf4j
public class TaskStepRegisterFactory {

    public TaskStepRegister getRegister(AppType appType) {
        switch (appType) {
            case SPRING_CLOUD:
                return getSpringCloudRegister();
            case NODEJS:
                return getNodeJsRegister();
            default:
                throw new IllegalArgumentException("unknow apptype: " + appType);
        }

    }

    private TaskStepRegister getSpringCloudRegister() {
        return new SpringCloudRegister();
    }

    private TaskStepRegister getNodeJsRegister() {
        return new NodeJsRegister();
    }

}
