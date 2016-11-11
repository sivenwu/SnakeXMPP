package com.snake.kit.exceptions;

/**
 * Created by Yuan on 2016/11/4.
 * Detail Exception for Snake
 */

public class SnakeException extends Exception {

    public SnakeException() {
    }

    public SnakeException(String detailMessage) {
        super(detailMessage);
    }

    public SnakeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SnakeException(Throwable throwable) {
        super(throwable);
    }


}
