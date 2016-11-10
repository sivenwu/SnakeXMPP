package com.snake.kit.core.mngservices;

/**
 * Created by Yuan on 2016/11/10.
 * Detail interface for RosterManager
 */

public interface IRosterManager {

    // 获取所有好友以及列表
    public void getAllRosters();

    // 删除好友
    public void deleteRoster();

    // 添加好友
    public void addRoster();

    // 更改好友信息
    public void updateRosterInfo();

    // 移动好友列表
    public void updateRosterByGroup();

    // 重命名分组
    public void setGroupName();

    // 添加分组
    public void addGroup();

    // 删除分组
    public void deleteGroup();

}
