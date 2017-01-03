package com.snake.kit.core.managers;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.snake.api.apptools.LogTool;
import com.snake.kit.core.data.SnakeRouter;
import com.snake.kit.core.handlers.SnakeServiceManager;
import com.snake.kit.core.mngservices.IRosterManager;
import com.snake.kit.interfaces.ISnakeRosterListener;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.snake.dbkit.bean.ContactModel;
import cn.snake.dbkit.manager.DBOperationManager;

/**
 * Created by Yuan on 2016/11/7.
 * Detail 好友管理
 */

public class SmackRosterManager extends BaseManager implements IRosterManager, RosterListener, RosterEntries {

    // info
    private Roster mRoster;
    //    private SparseArray mUserList; // key ：（主键） value ：jid
    private ArrayMap mUserList;

    private DBOperationManager dbOperationManager;

    public SmackRosterManager(Context context, SnakeServiceLetterListener mLetterListener, AbstractXMPPConnection mConnection) {
        super(context, mLetterListener, mConnection);
        mRoster = Roster.getInstanceFor(this.mConnection);
        // 默认添加好友需要询问
        mRoster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        mUserList = new ArrayMap();

        // 初始化数据库操作
        initDbOperationManager();
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
        // 好友状态变化拉
        LogTool.d("presenceChanged");

        ContactModel contactModel = null;

        String jid = presence.getFrom();
        contactModel = (ContactModel) mUserList.get(jid);
        boolean isAvailable = processPresence(presence);

        if (contactModel != null){
            // 获取新的状态
            contactModel.setIsOnline(Boolean.toString(isAvailable));
            mUserList.put(jid,contactModel);

            // 更新数据库
            updateContact(contactModel);
        }else{
            Object obj = buildContact();
            if (obj != null) {
                contactModel = (ContactModel) obj;
                contactModel.setJid(jid);
                contactModel.setIsOnline(Boolean.toString(isAvailable));
            }

            mUserList.put(jid,contactModel);
            // 更新数据库
            updateContact(contactModel);
        }

    }

    @Override
    public void rosterEntires(Collection<RosterEntry> rosterEntries) {

        List contacts = new ArrayList<ContactModel>();

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

            if (!TextUtils.isEmpty(jid)) {

                Object obj = buildContact();
                if (obj !=null) {

                    boolean isAvailable = processPresence(mRoster.getPresence(jid));

                    ContactModel contactModel = (ContactModel) obj;
                    contactModel.setJid(entry.getUser());
                    contactModel.setUserName(entry.getName());
                    contactModel.setGroupName(gourpName);
                    contactModel.setIsOnline(Boolean.toString(isAvailable));
                    // 设置状态
                    long userId = 0L;
                    addContact(contactModel);

//                    mUserList.put((int) userId,entry);
                    mUserList.put(entry.getUser(),contactModel);
                    contacts.add(contactModel);
                }
            }
        }

        getmLetterListener().sendHandlerLetter(SnakeServiceManager.HANDLLER_CODE_GET_ROSETER,contacts);
    }

    @Override
    public void setSubscriptionMode(Roster.SubscriptionMode subscriptionMode) {
        mRoster.setSubscriptionMode(subscriptionMode);
    }

    @Override
    public void getAllRosters(ISnakeRosterListener iSnakeRosterListener) {

        if (!mRoster.isLoaded()) {
            try {
                mRoster.reloadAndWait();
            } catch (SmackException.NotLoggedInException |
                    SmackException.NotConnectedException |
                    InterruptedException e) {
                e.printStackTrace();
                // 失败直接数据库读取
//                DBOperationManager.get().
            }
        }
        mRoster.getEntriesAndAddListener(this, this);
    }

    @Override
    public void deleteRoster(String user) {
        RosterEntry entry = mRoster.getEntry(user);
        if (entry != null) {
            try {
                if (mUserList.containsKey(user)) mUserList.remove(user);
                mRoster.removeEntry(entry);
                // 删除数据库
                deleteContact((ContactModel) mUserList.get(user));
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

                Object obj = buildContact();
                if (obj != null){
                    ContactModel model = (ContactModel) obj;
                    model.setJid(user);
                    model.setUserName(name);
                    model.setGroupName(groupName[0]);

                    addContact(model);
                    mUserList.put(user,model);
                }

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
    private boolean processPresence(Presence presence) {

        boolean isAvailable = false;

        Presence.Mode mode = presence.getMode();
        Presence.Type type = presence.getType();

        switch (type) {
            case available:
                break;
            case unavailable:
                isAvailable = false;
                break;
            case subscribe:
                break;
            case unsubscribe:
                break;
            case unsubscribed:
                break;
            default:
                isAvailable = true;
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
        return isAvailable;
    }

    private Object buildContact(){
        return SnakeRouter.instance().dbObject("ContactModel");
    }

    // -- db opreation
    private void updateContact(ContactModel contactModel){
        if (isUseDblibrary()){
            dbOperationManager.update(contactModel);
        }
    }

    private void addContact(ContactModel contactModel){
        if (isUseDblibrary()){
            dbOperationManager.update(contactModel);
        }
    }

    private void deleteContact(ContactModel contactModel){
        if (isUseDblibrary()){
            dbOperationManager.delete(contactModel);
        }
    }

    private void initDbOperationManager(){
        if (SnakeRouter.instance().isUseDbLibrary())
            dbOperationManager = SnakeRouter.instance().getDbLibarayKit();
    }

    private boolean isUseDblibrary(){
        return SnakeRouter.instance().isUseDbLibrary();
    }

}
