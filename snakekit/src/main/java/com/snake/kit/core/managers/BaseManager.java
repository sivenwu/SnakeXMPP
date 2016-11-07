package com.snake.kit.core.managers;

import android.content.Context;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * Created by Yuan on 2016/11/7.
 * Detail
 */

public class BaseManager {

    private Context context;
    private AbstractXMPPConnection mConnection;

    public BaseManager(Context context, AbstractXMPPConnection mConnection) {
        this.context = context;
        this.mConnection = mConnection;
    }

}
