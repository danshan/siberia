package com.shanhh.siberia.client.dto.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author shanhonghao
 * @since 2018-05-28 16:16
 */
public enum TaskStatus {
    UNKNOWN("UNKNOWN", "未知"),
    CREATED("CREATED", "已创建"),
    OK("OK", "成功"),
    SERVICING("SERVICING", "CLI 处理中"),
    RUNNING("RUNNING", "运行中"),
    FAIL("FAIL", "失败"),
    ROLLBACK("ROLLBACK", "回滚"),;

    public final String value;
    public final String desc;

    TaskStatus(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static TaskStatus of(String value) {
        for (TaskStatus a : TaskStatus.values()) {
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
