package com.snake.kit.core.managers;

import android.content.Context;

import org.jivesoftware.smack.AbstractXMPPConnection;

/**
 * Created by Yuan on 2016/11/9.
 * Detail 管理自己的信息的管理器，例如头像、状态、用户昵称、注册等...
 */

public class SmackOnselfManager extends BaseManager{

    public SmackOnselfManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);
    }

}
