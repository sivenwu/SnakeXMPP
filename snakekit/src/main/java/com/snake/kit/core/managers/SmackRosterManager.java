package com.snake.kit.core.managers;

import android.content.Context;

import com.snake.kit.apptools.LogTool;
import com.snake.kit.core.mngservices.IRosterManager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
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

public class SmackRosterManager  extends BaseManager implements IRosterManager,RosterListener,RosterEntries{

    // info
    private Roster mRoster;

    public SmackRosterManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);
    }

    public void entriesAdded(Collection<String> addresses) {
        LogTool.d("entriesAdded");
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        LogTool.d("entriesUpdated");
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        LogTool.d("entriesDeleted");
    }

    @Override
    public void presenceChanged(Presence presence) {
        LogTool.d("presenceChanged");
    }

    @Override
    public void rosterEntires(Collection<RosterEntry> rosterEntries) {
        // 处理获取RosterEntry
        for (RosterEntry rosterEntry : rosterEntries){
            String gourpName = "";
            String userName;
            String jid;
            for (RosterGroup rosterGroup :  rosterEntry.getGroups()){
                gourpName = rosterGroup.getName();
            }

            userName = rosterEntry.getName();
            jid = rosterEntry.getUser();

            LogTool.d("gourpName " + gourpName +" userName "+userName +" jid "+jid);
        }
    }

    @Override
    public void getAllRosters() {
        mRoster = Roster.getInstanceFor(this.mConnection);

        if (!mRoster.isLoaded()){
            try {
                mRoster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mRoster.getEntriesAndAddListener(this,this);
    }

    @Override
    public void deleteRoster() {

    }

    @Override
    public void addRoster() {

    }

    @Override
    public void updateRosterInfo() {

    }

    @Override
    public void updateRosterByGroup() {

    }

    @Override
    public void setGroupName() {

    }

    @Override
    public void addGroup() {

    }

    @Override
    public void deleteGroup() {

    }
}
