package com.sanke.sample;

import android.content.Context;
import android.widget.Toast;

import com.snake.api.data.MessageModel;
import com.snake.kit.core.receivers.MessageReceiver;


/**
 * Created by Yuan on 2016/11/29.
 * Detail 测试广播接收器
 */

public class MyMessageReceiver extends MessageReceiver {

    @Override
    public void onMessage(Context context, MessageModel message, String messag) {
//        ChatInfoModel model = new ChatInfoModel();
//        model.setType(message.getType());
//        model.setUserId(ChatAccountHelper.getUserId());
//        model.setJid(message.getFrom());
//        model.setMessage(message.getBody());
//        DBOperationManager.get().insert(model);
        Toast.makeText(context, messag, Toast.LENGTH_SHORT).show();
    }
}
