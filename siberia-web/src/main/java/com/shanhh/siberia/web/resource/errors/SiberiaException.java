package com.shanhh.siberia.web.resource.errors;

/**
 * @author dan.shan
 * @since 2018-03-20 11:57
 */
public class SiberiaException extends RuntimeException {
    public SiberiaException(String message) {
        super(message);
    }

    public SiberiaException(String message, Throwable cause) {
        super(message, cause);
    }
}
