package com.snake.kit.exceptions;

/**
 * Created by Yuan on 2016/11/4.
 * Detail RuntimeException for Snake
 */

public class SnakeRuntimeException extends RuntimeException {

    public SnakeRuntimeException() {
    }

    public SnakeRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public SnakeRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SnakeRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
