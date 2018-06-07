package com.shanhh.siberia.web.service.ansible;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * @author Dan
 * @since 2016-08-31 18:11
 */
@Slf4j
public class CommandThread extends Thread {

    /**
     * 标准输出流
     */
    @Setter
    @Getter
    private LogOutputStream stdOut;

    /**
     * 错误输出流
     */
    @Setter
    @Getter
    private LogOutputStream stdErr;

    /**
     * command line watcher
     */
    @Getter
    private ExecuteWatchdog watchdog;

    @Getter
    private DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

    @Setter
    @Getter
    private Executor executor = new DefaultExecutor();

    @Setter
    @Getter
    private File workingDir;

    @Setter
    @Getter
    private int[] expectExitCodes = null;

    @Getter
    private boolean keepRunning = true;
    @Getter
    private boolean processDestroyed = false;
    @Getter
    private boolean processStarted = false;
    @Getter
    private int exitCode = -1;
    @Getter
    private ExecuteException executeException;

    @Getter
    private CommandLine cmdline;

    public CommandThread(
            final String executable,
            Collection<String> args,
            Map<String, Object> substitutionMap,
            File workingDir,
            long timeoutMs
    ) {

        cmdline = new CommandLine(executable);
        if (!CollectionUtils.isEmpty(args)) {
            cmdline.addArguments(args.toArray(new String[args.size()]));
        }
        if (!CollectionUtils.isEmpty(substitutionMap)) {
            cmdline.setSubstitutionMap(substitutionMap);
        }

        this.watchdog = new ExecuteWatchdog(timeoutMs);
        this.workingDir = workingDir;
    }

    @Override
    public void run() {
        executor.setStreamHandler(new PumpStreamHandler(stdOut, stdErr));
        executor.setWatchdog(watchdog);
        executor.setWorkingDirectory(workingDir);

        if (this.expectExitCodes != null) {
            executor.setExitValues(expectExitCodes);
        }

        try {
            log.info("exec cmdline: {}", cmdline.toString());
            executor.execute(cmdline, resultHandler);
            processStarted = true;
        } catch (Exception e) {
            log.error("exec cmdline: " + cmdline.toString(), e);
            return;
        }

        try {
            while (keepRunning) {
                if (resultHandler.hasResult()) {
                    exitCode = resultHandler.getExitValue();
                    executeException = resultHandler.getException();
                    return;
                } else {
                    sleep(100);
                }
            }

            if (resultHandler.hasResult()) {
                exitCode = resultHandler.getExitValue();
                executeException = resultHandler.getException();
                return;
            }
        } catch (InterruptedException e) {
            log.error("command line failed: " + cmdline.toString(), e);
        }

        watchdog.destroyProcess();
        processDestroyed = true;
    }

    public void stopRunning() {
        this.keepRunning = false;
    }

}
