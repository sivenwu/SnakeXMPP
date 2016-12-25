package com.snake.kit.core.managers;

import android.content.Context;

import com.snake.kit.interfaces.SnakeServiceLetterListener;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * Created by Yuan on 2016/11/7.
 * Detail Multi User Chat 多人聊天管理器
 */

public class SmackMucManager  extends BaseManager{

    public SmackMucManager(Context context, SnakeServiceLetterListener mLetterListener, AbstractXMPPConnection mConnection) {
        super(context, mLetterListener, mConnection);
    }
}
