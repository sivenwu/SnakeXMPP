package com.snake.kit.interfaces;

/**
 * Created by Yuan on 2016/12/25.
 * Detail For listener ,Manager send some letters to SnakeService
 */


public interface SnakeServiceLetterListener {

    public void sendHandlerLetter(int code);

    public void sendHandlerLetter(int code,Object object);


}
