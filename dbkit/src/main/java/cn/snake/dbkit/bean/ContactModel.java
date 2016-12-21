package cn.snake.dbkit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenyk on 2016/12/20.
 */
@Entity
public class ContactModel {
    @Id(autoincrement = true)
    public Long _id;
    public String userId;
    public String jid;
    public String userName;
    public String lastMessage;
    public long lastTime;
    public String headPicUrl;
    public String mobile;
    public int newMessageCounts = 0;
    @Generated(hash = 423344210)
    public ContactModel(Long _id, String userId, String jid, String userName,
            String lastMessage, long lastTime, String headPicUrl, String mobile,
            int newMessageCounts) {
        this._id = _id;
        this.userId = userId;
        this.jid = jid;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
        this.headPicUrl = headPicUrl;
        this.mobile = mobile;
        this.newMessageCounts = newMessageCounts;
    }
    @Generated(hash = 1326690138)
    public ContactModel() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getJid() {
        return this.jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getLastMessage() {
        return this.lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    public long getLastTime() {
        return this.lastTime;
    }
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
    public String getHeadPicUrl() {
        return this.headPicUrl;
    }
    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public int getNewMessageCounts() {
        return this.newMessageCounts;
    }
    public void setNewMessageCounts(int newMessageCounts) {
        this.newMessageCounts = newMessageCounts;
    }

}
