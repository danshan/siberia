package com.shanhh.siberia.client.dto.pipeline;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author shanhonghao
 * @since 2018-06-20 09:37
 */
public enum PipelineStatus {

    UNKNOWN(0, "unknown"),
    RUNNING(1, "running"),
    ARCHIVED(2, "archived"),;

    public final int value;
    public final String desc;

    PipelineStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static PipelineStatus of(int value) {
        for (PipelineStatus a : PipelineStatus.values()) {
            if (a.value == value) {
                return a;
            }
        }
        return RUNNING;
    }

    @JsonValue
    public Map<String, Object> value() {
        return ImmutableMap.<String, Object>builder()
                .put("value", value)
                .put("desc", desc)
                .build();
    }
}
