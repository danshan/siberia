package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.web.service.ansible.AnsibleResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author dan
 * @since 2017-01-25 11:45
 */
public interface AnsibleService {

    AnsibleResult execPlaybook(TaskDTO task, String inventory, String yamlName, Map<String, Object> vars) throws IOException, InterruptedException;

    void terminalSibTaskPlaybook(int taskId);

    List<String> readLogById(int taskId) throws IOException;

}
