package com.snake.kit.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.snake.api.apptools.LogTool;
import com.snake.api.data.MessageModel;


/**
 * Created by Yuan on 2016/11/14.
 * Detail 消息广播接收器
 */

public abstract class MessageReceiver extends BroadcastReceiver {

    public static String SNAKE_MESSAGE_ACTION = "com.snake.kit.core.receiver.action";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getExtras() != null) {
                MessageModel message = intent.getExtras().getParcelable("message");
                LogTool.i(message.toString());
                onMessage(context, message, message.getBody());
            }
        }
    }

    // 外抛抽象方法
    public abstract void onMessage(Context context, MessageModel message, String messag);

}
