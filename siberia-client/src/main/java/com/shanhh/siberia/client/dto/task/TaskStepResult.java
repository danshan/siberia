package com.shanhh.siberia.client.dto.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author Dan
 * @since 2016-06-03 03:05
 */
public enum TaskStepResult {
    UNKNOWN("UNKNOWN", "unknown"),
    OK("OK", "success"),
    FAILED("FAILED", "failed"),
    ERROR("ERROR", "error"),;

    public final String value;
    public final String desc;

    TaskStepResult(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static TaskStepResult of(String value) {
        for (TaskStepResult a : TaskStepResult.values()) {
            if (a.value.equals(value)) {
                return a;
            }
        }
        return UNKNOWN;
    }

    @JsonValue
    public Map<String, Object> value() {
        return ImmutableMap.<String, Object>builder()
                .put("value", value)
                .put("desc", desc)
                .build();
    }
}
