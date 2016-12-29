package cn.snake.dbkit.manager;

import cn.snake.dbkit.bean.ChatInfoModel;

/**
 * Created by chenyk on 2016/12/29.
 * 聊天消息管理类
 */

public class ChatMessageManager {
    public void sendMessage(int type) {
        // TODO: 2016/12/29 发送聊天消息

        //保存聊天信息至数据库
        DBOperationManager.get().insert(new ChatInfoModel());
    }
}
