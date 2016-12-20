package com.snake.kit.apptools;


import com.snake.api.data.MessageModel;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by Yuan on 2016/11/14.
 * Detail 消息处理
 *
 * starting
 * 某人开始一个对话，但是你还没有参与进来
 * active
 * 你正参与在对话中，当前你没有组织你的消息，而是在关注
 * composing
 * 你正在组织一个消息
 * paused
 * 你开始组织一个消息，但由于某个原因而停止组织消息
 * inactive
 * 你一段时间里没有参与这个对话
 * gone
 *  你参与的这个对话已经结束
 *
 */

public class MessageDealUtil {

    // 处理后只有两种状态 正在输入，普通状态
    private static final String INPUTING = "INPUTING";
    private static final String COMMON = "COMMON";

    public static MessageModel dealMessage(Message message){
        Message.Type type = message.getType();

        MessageModel realMsg = new MessageModel();

        realMsg.setType(type.toString());
        realMsg.setThread(message.getThread());
        realMsg.setFrom(message.getFrom());
        realMsg.setTo(message.getTo());
        realMsg.setId(message.getStanzaId());
        realMsg.setState(COMMON);

        if (message.getBody() != null){
            realMsg.setBody(message.getBody());
        }else{
            // 处理状态消息
            if (message.toString().contains("<composing")){
                realMsg.setState(INPUTING);
            }
        }

        return realMsg;
    }

}
