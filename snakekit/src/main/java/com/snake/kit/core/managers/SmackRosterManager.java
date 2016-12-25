package com.snake.kit.core.managers;

import android.content.Context;
import android.util.SparseArray;

import com.snake.api.apptools.LogTool;
import com.snake.kit.core.mngservices.IRosterManager;
import com.snake.kit.interfaces.SnakeServiceLetterListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntries;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;

/**
 * Created by Yuan on 2016/11/7.
 * Detail 好友管理
 */

public class SmackRosterManager extends BaseManager implements IRosterManager, RosterListener, RosterEntries {

    // info
    private Roster mRoster;
    private SparseArray mUserList; // key ：（jid 对应的主键） value ：RosterEntry

    public SmackRosterManager(Context context, SnakeServiceLetterListener mLetterListener, AbstractXMPPConnection mConnection) {
        super(context, mLetterListener, mConnection);
        mRoster = Roster.getInstanceFor(this.mConnection);
        // 默认添加好友需要询问
        mRoster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        mUserList = new SparseArray();
    }


    public void entriesAdded(Collection<String> addresses) {
        // 添加好友
        LogTool.d("entriesAdded");
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        LogTool.d("entriesUpdated");
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        // 删除好友
        LogTool.d("entriesDeleted");
    }

    @Override
    public void presenceChanged(Presence presence) {
        // 好友状态变化
        LogTool.d("presenceChanged");
    }

    @Override
    public void rosterEntires(Collection<RosterEntry> rosterEntries) {
        // 处理获取RosterEntry
        for (RosterEntry entry : rosterEntries) {
            String gourpName = "";
            String userName;
            String jid;
            for (RosterGroup rosterGroup : entry.getGroups()) {
                gourpName = rosterGroup.getName();
            }

            userName = entry.getName();
            jid = entry.getUser();

            LogTool.d("gourpName " + gourpName + " userName " + userName + " jid " + jid);
//            mUserList.put(jid,entry);

            processPresence(mRoster.getPresence(jid));
        }
    }

    @Override
    public void setSubscriptionMode(Roster.SubscriptionMode subscriptionMode) {
        mRoster.setSubscriptionMode(subscriptionMode);
    }

    @Override
    public void getAllRosters() {
        if (!mRoster.isLoaded()) {
            try {
                mRoster.reloadAndWait();
            } catch (SmackException.NotLoggedInException |
                    SmackException.NotConnectedException |
                    InterruptedException e) {
                e.printStackTrace();
            }
        }
        mRoster.getEntriesAndAddListener(this, this);
    }

    @Override
    public void deleteRoster(String user) {
        RosterEntry entry = mRoster.getEntry(user);
        if (entry != null) {
            try {
                mRoster.removeEntry(entry);
                // 删除数据库
            } catch (SmackException.NotLoggedInException |
                    SmackException.NoResponseException |
                    XMPPException.XMPPErrorException |
                    SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addRoster(String user, String name, String groupName) {
        addRosyer(user, name, new String[]{groupName});
    }

    private void addRosyer(String user, String name, String[] groupName) {
        if (mConnection.isAuthenticated()) {
            try {
                mRoster.createEntry(user, name, groupName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateRosterInfo() {
        // 不支持.
    }

    @Override
    public void updateRosterByGroup(String user, String curGroup, String mvGroup) {
        RosterGroup cGroup = mRoster.getGroup(curGroup);
        RosterGroup mGroup = mRoster.getGroup(mvGroup);
        RosterEntry entry = null;
        if (cGroup != null) {
            entry = cGroup.getEntry(user);
            if (entry != null) {
                try {
                    if (mGroup == null) {
                        // 不存在该分组 即创建
                        mGroup = mRoster.createGroup(mvGroup);
                    }
                    cGroup.removeEntry(entry);
                    mGroup.addEntry(entry);
                } catch (SmackException.NoResponseException |
                        SmackException.NotConnectedException |
                        XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setGroupName(String groupName, String modifyName) {
        RosterGroup group = mRoster.getGroup(groupName);
        if (group != null) {
            try {
                group.setName(modifyName);
            } catch (SmackException.NotConnectedException |
                    SmackException.NoResponseException |
                    XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addGroup(String groupName) {
        mRoster.createGroup(groupName);
    }

    @Override
    public void deleteGroup(String groupName) {
        // 不支持..

    }

    //---------------------------------------------------------------------------------------------

    // 获取用户状态
    private void processPresence(Presence presence) {

        Presence.Mode mode = presence.getMode();
        Presence.Type type = presence.getType();

        switch (type) {
            case available:

                break;
            case unavailable:

                break;
            case subscribe:
                break;
            case unsubscribe:
                break;
            case unsubscribed:
                break;

        }

        switch (mode) {
            case available:

                break;
            case chat:

                break;
            case away:

                break;
            case xa:

                break;
            case dnd:

                break;

        }
    }
}
