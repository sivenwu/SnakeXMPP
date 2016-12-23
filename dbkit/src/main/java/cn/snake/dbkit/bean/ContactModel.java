package cn.snake.dbkit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenyk on 2016/12/20.
 * 对聊和群聊共用
 */
@Entity
public class ContactModel {
    @Id(autoincrement = true)
    public Long _id;
    public String userId;
    public int groupId;//群id
    public String groupName;//群名称
    public String groupHeadPicUrl;//群头像
    public String jid;
    public String userName;
    public String lastMessage;
    public long lastTime;
    public String headPicUrl;
    public String mobile;
    @Generated(hash = 196618442)
    public ContactModel(Long _id, String userId, int groupId, String groupName,
            String groupHeadPicUrl, String jid, String userName, String lastMessage,
            long lastTime, String headPicUrl, String mobile) {
        this._id = _id;
        this.userId = userId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupHeadPicUrl = groupHeadPicUrl;
        this.jid = jid;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
        this.headPicUrl = headPicUrl;
        this.mobile = mobile;
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
    public int getGroupId() {
        return this.groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupHeadPicUrl() {
        return this.groupHeadPicUrl;
    }
    public void setGroupHeadPicUrl(String groupHeadPicUrl) {
        this.groupHeadPicUrl = groupHeadPicUrl;
    }

}
