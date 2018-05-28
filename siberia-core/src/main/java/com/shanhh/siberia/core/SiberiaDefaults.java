package com.shanhh.siberia.core;

/**
 * @author shanhonghao
 * @since 2018-05-24 09:30
 */
public interface SiberiaDefaults {

    interface Async {
        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }

    interface Metrics {
        interface Jmx {
            boolean enabled = true;
        }

        interface Graphite {
            boolean enabled = false;
            String host = "localhost";
            int port = 2003;
            String prefix = "jhipsterApplication";
        }

        interface Prometheus {
            boolean enabled = false;
            String endpoint = "/prometheusMetrics";
        }

        interface Logs {
            boolean enabled = false;
            long reportFrequency = 60;
        }
    }

    interface Schedule {
        int poolSize = 10;
    }
}
