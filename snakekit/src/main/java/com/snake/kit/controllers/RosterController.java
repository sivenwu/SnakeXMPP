package com.snake.kit.controllers;

import com.snake.kit.interfaces.ISnakeRosterListener;

import org.jivesoftware.smack.roster.Roster;

/**
 * Created by Yuan on 2016/11/10.
 * Detail 好友控制器
 */

public final class RosterController extends APPController{

    public static void getAllRosters(ISnakeRosterListener i){
        if (isRunning())
            getmSnakeService().getAllRosters(i);
    }

    public static void addRoster(String user,String name,String groupName) {
        if (isRunning())
            getmSnakeService().addRoster(user,name,groupName);
    }

    public  static void setSubscriptionMode(Roster.SubscriptionMode mode) {
        if (isRunning())
            getmSnakeService().setSubscriptionMode(mode);
    }

    public static void deleteRoster(String user){
        if (isRunning())
            getmSnakeService().deleteRoster(user);
    }

    public static void updateRosterInfo(){
        if (isRunning())
            getmSnakeService().updateRosterInfo();
    }

    public static void updateRosterByGroup(String user,String curGroup,String mvGroup){
        if (isRunning())
            getmSnakeService().updateRosterByGroup(user,curGroup,mvGroup);
    }

    public static void setGroupName(String groupName,String modifyName){
        if (isRunning())
            getmSnakeService().setGroupName(groupName,modifyName);
    }

    public static void addGroup(String groupName){
        if (isRunning())
            getmSnakeService().addGroup(groupName);
    }

    public static void deleteGroup(String groupName){
        if (isRunning())
            getmSnakeService().deleteGroup(groupName);
    }

}
