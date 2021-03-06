package com.shanhh.siberia.client.dto.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by fanwenbin on 14-12-19.
 */
public enum AppType {

    UNKNOWN("unknown", "未知"),
    SPRING_CLOUD("spring-cloud", "spring-cloud"),
    NODEJS("nodejs", "nodejs"),;

    public final String value;
    public final String desc;

    AppType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static AppType of(String value) {
        String vv = StringUtils.trimToEmpty(value).toLowerCase();
        for (AppType a : AppType.values()) {
            if (a.value.equals(vv)) {
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
