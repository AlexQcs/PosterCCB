package com.hc.posterccb.exception;

/**
 * Created by alex on 2017/7/8.
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
