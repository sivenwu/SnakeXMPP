package com.snake.kit.core;

/**
 * Created by Yuan on 2016/11/4.
 * Detail
 */

public interface Smack {

    /**
     * 登录
     *
     * @param account
     * @param passwords
     * @return
     */
    boolean login(String account, String passwords);

    /**
     * 注销登录
     *
     * @return
     */
    boolean logout();

    /**
     * 是否连接服务器
     *
     * @return
     */
    boolean isAuthenticated();

    /**
     * 添加好友
     *
     * @param user
     * @param alias
     * @param group
     */
    void addRosterItem(String user, String alias, String group);

    /**
     * 删除好友
     *
     * @param user
     * @
     */
    void removeRosterItem(String user);

    /**
     * 修改好友名称
     *
     * @param user
     * @param newName
     * @
     */
    void renameRosterItem(String user, String newName);

    /**
     * 移动好友到新分组
     *
     * @param user
     * @param group
     * @
     */
    void moveRosterItemToGroup(String user, String group);

    /**
     * 重命名分组
     *
     * @param group
     * @param newGroup
     */
    void renameRosterGroup(String group, String newGroup);

    /**
     * 添加好友，重新
     *
     * @param user
     */
    void requestAuthorizationForRosterItem(String user);

    /**
     * 添加新分组
     *
     * @param group
     */
    void addRosterGroup(String group);

    /**
     * 设置当前在线状态
     */
    void setStatusFromConfig();

    /**
     * 发送消息
     *
     * @param user
     * @param message
     */
    void sendMessage(String user, String message);

    /**
     * 发送心跳包，保持长连接，通过闹钟控制，定时发送
     */
    void sendServerPing();

    /**
     * 从jid中获取好友名
     *
     * @param jid
     * @return
     */
    String getNameForJID(String jid);

    /**
     * 注册新的用户
     */
    int registerUser(String username, String password);

}
