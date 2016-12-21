package cn.snake.dbkit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenyk on 2016/12/15.
 */
@Entity
public class ChatInfoModel {
    @Id(autoincrement = true)
    public Long _id;
    public String userId;
    public String jid;
    public String type;
    public String to;
    public String from;
    public String message; //content
    private String state;//commom , inputing
    public String messageStatus; //send success: "1" , send fail:"-1" ,sending: "0"
    @Generated(hash = 1428097934)
    public ChatInfoModel(Long _id, String userId, String jid, String type,
            String to, String from, String message, String state,
            String messageStatus) {
        this._id = _id;
        this.userId = userId;
        this.jid = jid;
        this.type = type;
        this.to = to;
        this.from = from;
        this.message = message;
        this.state = state;
        this.messageStatus = messageStatus;
    }
    @Generated(hash = 1033339016)
    public ChatInfoModel() {
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
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getMessageStatus() {
        return this.messageStatus;
    }
    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}
