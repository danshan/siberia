package com.shanhh.siberia.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author dan.shan
 * @since 2018-03-03 15:03
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

    public static final String TASKS_LIST = "/tasks/list";
    public static final String APP_LOCK_LIST= "/apps/locks/list";
    public static final String LOGS_ANSIBLE = "/logs/ansible/%s";
    public static final String LOGS_SIBERIA = "/logs/siberia/%s";
    public static final String PIPELINES = "/pipelines/%s";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/wsendpoint")
                .setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/tasks", "/pipelines", "/envs", "/logs");
        registry.setApplicationDestinationPrefixes("/app-receive");
    }

}
