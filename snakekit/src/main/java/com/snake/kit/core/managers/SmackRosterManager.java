package com.snake.kit.core.managers;

import android.content.Context;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Collection;

/**
 * Created by Yuan on 2016/11/7.
 * Detail
 */

public class SmackRosterManager  extends BaseManager{

    public SmackRosterManager(Context context, AbstractXMPPConnection mConnection) {
        super(context, mConnection);
    }

    public void getAllRoster(){
        Roster roster = Roster.getInstanceFor(this.mConnection);
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println(entry);
        }
    }
}
