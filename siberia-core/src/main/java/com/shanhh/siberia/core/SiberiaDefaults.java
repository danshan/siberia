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
}
