package com.sanke.sample;

import android.content.Context;
import android.widget.Toast;

import com.snake.kit.core.data.MessageModel;
import com.snake.kit.core.receivers.MessageReceiver;

/**
 * Created by Yuan on 2016/11/29.
 * Detail 测试广播接收器
 */

public class MyMessageReceiver extends MessageReceiver {

    @Override
    public void onMessage(Context context, MessageModel message, String messag) {
        Toast.makeText(context, messag, Toast.LENGTH_SHORT).show();
    }
}
