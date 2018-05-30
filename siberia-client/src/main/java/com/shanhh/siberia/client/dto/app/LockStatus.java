package com.shanhh.siberia.client.dto.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Dan
 * @since 2016-06-08 16:10
 */
public enum LockStatus {
    LOCKED(1, "locked"),
    UNLOCKED(0, "unlocked"),;

    public final int value;
    public final String desc;

    LockStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static LockStatus of(int value) {
        for (LockStatus a : LockStatus.values()) {
            if (a.value == value) {
                return a;
            }
        }
        return LOCKED;
    }

    @JsonValue
    public int value() {
        return value;
    }
}
