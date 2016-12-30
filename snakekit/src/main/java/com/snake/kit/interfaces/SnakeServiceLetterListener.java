package com.snake.kit.interfaces;

/**
 * Created by Yuan on 2016/12/25.
 * Detail For listener ,Manager send some letters to SnakeService
 */

public interface SnakeServiceLetterListener {

    void sendHandlerLetter(int code);

    void sendHandlerLetter(int code, Object object);

}
