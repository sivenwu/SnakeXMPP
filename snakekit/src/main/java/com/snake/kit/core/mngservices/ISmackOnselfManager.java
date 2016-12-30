package com.snake.kit.core.mngservices;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by Yuan on 2016/12/20.
 * Detail interface for SmackOnselfManager
 */

public interface ISmackOnselfManager {

    // 连接配置
    public void connectionConfig();

    // 登录
    public void login() throws Exception ;
    public void login(String account,String password) throws IOException, XMPPException, SmackException, Exception;

    // 登出
    public void logout();

    // 注册用户
    public void registerUser(String account, String password);

    // 修改登录密码
    public void modifyPassword();

}
