package com.shanhh.siberia.web.config;

import com.shanhh.siberia.core.SiberiaProperties;
import com.shanhh.siberia.core.async.ExceptionHandlingAsyncTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
@Slf4j
public class AsyncConfiguration implements AsyncConfigurer {

    @Resource
    private SiberiaProperties siberiaProperties;

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(siberiaProperties.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(siberiaProperties.getAsync().getMaxPoolSize());
        executor.setQueueCapacity(siberiaProperties.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix("Siberia-Executor-");
        return new ExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
