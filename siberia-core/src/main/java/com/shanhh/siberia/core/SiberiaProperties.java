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
    @Getter
    private final Metrics metrics = new Metrics();
    @Getter
    private final Schedule schedule = new Schedule();

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Async {
        private int corePoolSize = SiberiaDefaults.Async.corePoolSize;
        private int maxPoolSize = SiberiaDefaults.Async.maxPoolSize;
        private int queueCapacity = SiberiaDefaults.Async.queueCapacity;
    }

    @NoArgsConstructor
    @Getter
    public static class Metrics {

        private final Jmx jmx = new Jmx();
        private final Graphite graphite = new Graphite();
        private final Prometheus prometheus = new Prometheus();
        private final Logs logs = new Logs();

        @Setter
        @Getter
        public static class Jmx {
            private boolean enabled = SiberiaDefaults.Metrics.Jmx.enabled;
        }

        @Setter
        @Getter
        public static class Graphite {
            private boolean enabled = SiberiaDefaults.Metrics.Graphite.enabled;
            private String host = SiberiaDefaults.Metrics.Graphite.host;
            private int port = SiberiaDefaults.Metrics.Graphite.port;
            private String prefix = SiberiaDefaults.Metrics.Graphite.prefix;
        }

        @Setter
        @Getter
        public static class Prometheus {
            private boolean enabled = SiberiaDefaults.Metrics.Prometheus.enabled;
            private String endpoint = SiberiaDefaults.Metrics.Prometheus.endpoint;
        }

        @Setter
        @Getter
        public static class Logs {
            private boolean enabled = SiberiaDefaults.Metrics.Logs.enabled;
            private long reportFrequency = SiberiaDefaults.Metrics.Logs.reportFrequency;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Schedule {
        private int poolSize = SiberiaDefaults.Schedule.poolSize;
    }
}
