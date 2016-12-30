package com.snake.kit.core.mngservices;

import com.snake.kit.interfaces.ISnakeRosterListener;

import org.jivesoftware.smack.roster.Roster;

/**
 * Created by Yuan on 2016/11/10.
 * Detail interface for RosterManager
 */

public interface IRosterManager {

    // 添加好友的权限
    void setSubscriptionMode(Roster.SubscriptionMode subscriptionMode);

    // 获取所有好友以及列表
    void getAllRosters(ISnakeRosterListener iSnakeRosterListener);

    // 删除好友
    void deleteRoster(String user);

    // 添加好友
    void addRoster(String user, String name, String groupName);

    // 更改好友信息
    void updateRosterInfo();

    // 移动好友列表
    void updateRosterByGroup(String user, String curGroup, String mvGroup);

    // 重命名分组
    void setGroupName(String groupName, String modifyName);

    // 添加分组
    void addGroup(String groupName);

    // 删除分组
    void deleteGroup(String groupName);

}
