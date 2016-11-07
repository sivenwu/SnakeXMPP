package com.snake.kit.core;

/**
 * Created by Yuan on 2016/11/4.
 * Detail
 */

public interface Smack {

    /**
     * 登录
     * @param account
     * @param passwords
     * @return
     */
    public boolean login(String account, String passwords);

    /**
     * 注销登录
     * @return
     */
    public boolean logout();

    /**
     * 是否连接服务器
     * @return
     */
    public boolean isAuthenticated();

    /**
     * 添加好友
     * @param user
     * @param alias
     * @param group
     */
    public void addRosterItem(String user, String alias, String group);

    /**
     * 删除好友
     * @param user
     * @
     */
    public void removeRosterItem(String user);

    /**
     * 修改好友名称
     * @param user
     * @param newName
     * @
     */
    public void renameRosterItem(String user, String newName);

    /**
     * 移动好友到新分组
     * @param user
     * @param group
     * @
     */
    public void moveRosterItemToGroup(String user, String group);

    /**
     * 重命名分组
     * @param group
     * @param newGroup
     */
    public void renameRosterGroup(String group, String newGroup);

    /**
     * 添加好友，重新
     * @param user
     */
    public void requestAuthorizationForRosterItem(String user);

    /**
     * 添加新分组
     * @param group
     */
    public void addRosterGroup(String group);

    /**
     * 设置当前在线状态
     */
    public void setStatusFromConfig();

    /**
     * 发送消息
     * @param user
     * @param message
     */
    public void sendMessage(String user, String message);

    /**
     * 发送心跳包，保持长连接，通过闹钟控制，定时发送
     */
    public void sendServerPing();

    /**
     * 从jid中获取好友名
     * @param jid
     * @return
     */
    public String getNameForJID(String jid);

    /**
     * 注册新的用户
     */
    public int registerUser(String username,String password);
    
}
