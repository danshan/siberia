package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.shanhh.siberia.client.dto.task.TaskDTO;
import com.shanhh.siberia.core.SiberiaProperties;
import com.shanhh.siberia.web.config.WebSocketConfiguration;
import com.shanhh.siberia.web.service.AnsibleService;
import com.shanhh.siberia.web.service.ansible.AnsibleOutputStream;
import com.shanhh.siberia.web.service.ansible.AnsibleResult;
import com.shanhh.siberia.web.service.ansible.CommandThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dan
 * @since 2017-01-25 11:51
 */
@Service
@Slf4j
public class AnsibleServiceImpl implements AnsibleService {

    private static final Map<Integer, CommandThread> RUNNING_THREADS = Maps.newLinkedHashMap();

    private final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private SiberiaProperties siberiaProperties;
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public AnsibleResult execPlaybook(TaskDTO task, String inventory, String yamlName, Map<String, Object> vars) throws IOException, InterruptedException {
        SiberiaProperties.Ansible ansible = siberiaProperties.getAnsible();
        HashMap<String, Object> substitutionMap = new HashMap<>();
        substitutionMap.put("yamlName", yamlName);

        List<String> args = Lists.newLinkedList();
        if (StringUtils.isNoneBlank(inventory)) {
            args.add("-i");
            args.add(StringUtils.defaultIfBlank(inventory, task.getEnv().getName()));
        }
        args.add("${yamlName}");
        if (!CollectionUtils.isEmpty(vars)) {
            vars.forEach((key, value) -> {
                args.add("-e");
                args.add(key + "=" + value);
            });
        }

        if (StringUtils.isNotBlank(ansible.getPrivateKey())) {
            args.add("--private-key=" + ansible.getPrivateKey());
        }

        CommandThread commandThread = new CommandThread(
                playbookBin(),
                args,
                substitutionMap,
                new File(ansible.getPlaybookPath()),
                Long.MAX_VALUE
        );

        String logfile = getLogfile(task, ansible);

        Files.append(commandThread.getCmdline().toString() + "\n", new File(logfile), Charsets.UTF_8);
        commandThread.setStdOut(new AnsibleOutputStream(logfile) {
            @Override
            protected void processLine(String line, int logLevel) {
                if (line.startsWith("fatal: ")) {
                    log.error(line);
                } else {
                    log.info(line);
                }
                appendToFile(line);
                pushToWebsocket(task.getId(), line);
            }
        });
        commandThread.setStdErr(new AnsibleOutputStream(logfile) {
            @Override
            protected void processLine(String line, int logLevel) {
                log.error(line);
                appendToFile(line);
                pushToWebsocket(task.getId(), line);
            }
        });

        try {
            RUNNING_THREADS.put(task.getId(), commandThread);
            commandThread.start();
            commandThread.join();

            log.info("exitCode = " + commandThread.getExitCode());
            log.info("exitCode = " + commandThread.getExecuteException());

            if (commandThread.isProcessStarted() && !commandThread.isProcessDestroyed()) {
                log.info("Process lanched successed");
            } else if (commandThread.isProcessStarted()) {
                log.warn("Process was launched, but process destroyed");
            } else {
                log.warn("Process not launched");
            }
        } finally {
            RUNNING_THREADS.remove(task.getId());
        }
        return new AnsibleResult(commandThread.getExitCode(), commandThread.getExecuteException());
    }

    private void pushToWebsocket(int taskId, String line) {
        String time = DateTime.now().toString(format);
        simpMessagingTemplate.convertAndSend(String.format(WebSocketConfiguration.LOGS_ANSIBLE, taskId),
                ImmutableMap.<String, String>builder().put("line", String.format("%s %s", time, line)).build());
    }

    @Override
    public void terminalSibTaskPlaybook(int taskId) {
        CommandThread commandThread = RUNNING_THREADS.get(taskId);
        if (commandThread != null) {
            commandThread.stopRunning();
        }
    }

    @Override
    public List<String> readLogById(int taskId) throws IOException {
        String logfile = String.format("%s/%d.log", siberiaProperties.getAnsible().getLogPath(), taskId);
        File file = new File(logfile);
        if (file.exists()) {
            return Files.readLines(new File(logfile), Charsets.UTF_8);
        } else {
            return Lists.newArrayList("log file not exists.");
        }
    }

    private String playbookBin() {
        Preconditions.checkState(StringUtils.isNotBlank(siberiaProperties.getAnsible().getAnsibleHome()), "ansible home should not be empty");
        String ansibleHome = siberiaProperties.getAnsible().getAnsibleHome();
        String playbookBin = ansibleHome.endsWith("/")
                ? (ansibleHome + "bin/ansible-playbook")
                : (ansibleHome + "/" + "bin/ansible-playbook");
        Preconditions.checkState(new File(playbookBin).exists(), "ansible-playbook not found: %s", playbookBin);
        return playbookBin;
    }

    private String getLogfile(TaskDTO task, SiberiaProperties.Ansible ansible) {
        Preconditions.checkState(StringUtils.isNotBlank(ansible.getLogPath()), "log path should not be empty");
        Preconditions.checkState(new File(ansible.getLogPath()).exists(), "log path not exists, %s", ansible.getLogPath());
        Preconditions.checkState(new File(ansible.getLogPath()).isDirectory(), "log path should be directory, %s", ansible.getLogPath());
        return String.format("%s/%d.log", ansible.getLogPath(), task.getId());
    }

}
