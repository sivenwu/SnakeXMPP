package com.snake.kit.core.managers;

import android.content.Context;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * Created by Yuan on 2016/11/7.
 * Detail 聊天信息处理管理器，包括推送消息
 */

public class SmackMessageManager extends BaseManager{

    public SmackMessageManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);
    }
}
