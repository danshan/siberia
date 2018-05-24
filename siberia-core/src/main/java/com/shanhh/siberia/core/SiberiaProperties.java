package com.shanhh.siberia.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Siberia.
 *
 * <p> Properties are configured in the application.yml file. </p>
 *
 * @author shanhonghao
 * @since 2018-05-24 09:26
 */
@ConfigurationProperties(prefix = "siberia", ignoreUnknownFields = false)
public class SiberiaProperties {

    @Getter
    private final Async async = new Async();

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Async {
        private int corePoolSize = SiberiaDefaults.Async.corePoolSize;
        private int maxPoolSize = SiberiaDefaults.Async.maxPoolSize;
        private int queueCapacity = SiberiaDefaults.Async.queueCapacity;
    }

}
