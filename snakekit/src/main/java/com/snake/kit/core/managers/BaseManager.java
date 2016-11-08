package com.snake.kit.core.managers;

import android.content.Context;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * Created by Yuan on 2016/11/7.
 * Detail 基础管理器...
 */

public class BaseManager {

    public Context context;
    public AbstractXMPPConnection mConnection;

    public BaseManager(Context context, AbstractXMPPConnection mConnection) {
        this.context = context;
        this.mConnection = mConnection;
    }



}
