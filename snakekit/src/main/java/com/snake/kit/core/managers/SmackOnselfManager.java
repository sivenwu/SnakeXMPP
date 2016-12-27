package com.snake.kit.core.managers;

import android.content.Context;

import com.snake.kit.core.handlers.SnakeServiceManager;
import com.snake.kit.core.mngservices.ISmackOnselfManager;
import com.snake.kit.interfaces.SnakeServiceLetterListener;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.iqregister.packet.Registration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuan on 2016/11/9.
 * Detail 管理自己的信息的管理器，例如头像、状态、用户昵称、注册等...
 */

public class SmackOnselfManager extends BaseManager implements ISmackOnselfManager {

    private String curAccount;
    private String curPassword;

    public SmackOnselfManager(Context context, SnakeServiceLetterListener mLetterListener, AbstractXMPPConnection mConnection) {
        super(context, mLetterListener, mConnection);
    }


    @Override
    public void login() throws Exception {
        login(curAccount,curPassword);
    }

    @Override
    public void login(String account, String password) {

        this.curAccount = account;
        this.curPassword = password;

        try {
            this.mConnection.login(account,password);

        } catch (Exception e) {
            getmLetterListener().sendHandlerLetter(SnakeServiceManager.HANDLER_CODE_LOGIN_FAILED,e);
        }
    }

    @Override
    public void registerUser(String account, String password) {

        Map<String, String> map = new HashMap<>();
        map.put("account",account);
        map.put("password",password);
        map.put("android","");

        Registration mRegistration = new Registration(map);
        mRegistration.setType(IQ.Type.set);
        mRegistration.setTo(this.mConnection.getServiceName());

        try {
            this.mConnection.sendStanza(mRegistration);

            StanzaFilter filter = new AndFilter(new StanzaIdFilter(mRegistration.getStanzaId()), new StanzaTypeFilter(IQ.class));
            PacketCollector collector = mConnection.createPacketCollector(filter);

            IQ result = (IQ) collector.nextResult(mConnection.getPacketReplyTimeout());
            collector.cancel();
            if (result == null || result.getType() == IQ.Type.error) {
                // register error
            } else {
                // register suceess!
            }

        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        // 目前注销方式 只能断开连接 重新连接登录
        this.mConnection.disconnect();
    }

    @Override
    public void modifyPassword() {

    }

    public boolean isLoginNow(){
        return (mConnection !=null && mConnection.isAuthenticated());
    }

    //------------- ConnectionListener -------------------------------------------------------------
}
