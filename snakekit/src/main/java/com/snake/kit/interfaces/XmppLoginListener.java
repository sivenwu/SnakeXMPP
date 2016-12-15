package com.snake.kit.interfaces;

/**
 * Created by Yuan on 2016/11/7.
 * Detail xmpp 连接情况回调
 */

public interface XmppLoginListener {
    // 鉴权成功回调
    void authenticated();

    // 失败统一抛出
    void onError(Exception e, String message);

}
