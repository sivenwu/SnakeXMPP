package cn.snake.dbkit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenyk on 2016/12/15.
 * 对聊和群聊共用
 */
@Entity
public class ChatInfoModel {
    public static final int TYPE_TEXT = 1;//文字消息
    public static final int TYPE_PICTURE = 2;//图片消息
    public static final int TYPE_VOICE = 3;//语音消息
    public static final int TYPE_VIDEO = 4;//视频消息

    public static final int STATUS_SEND_FAIL = 1;
    public static final int STATUS_SEND_SENDING = 2;
    public static final int STATUS_SEND_SUCCESS = 3;

    @Id(autoincrement = true)
    public Long _id;
    public String userId;//用户id
    public int groupId;//群id , -1 -> does not group chat
    public String jid;//群聊时且为本人信息时， jid = userId
    public String type;//text,picture,voice,video
    public String isFrom;//是否来自对方,"true","false"
    public int isRead;//1->read,2->dose not read
    public String message; //content
    private String state;//commom , inputing
    public String messageStatus; //send success: "3" , send fail:"1" ,sending: "2"

    @Generated(hash = 1029089737)
    public ChatInfoModel(Long _id, String userId, int groupId, String jid,
                         String type, String isFrom, int isRead, String message, String state,
                         String messageStatus) {
        this._id = _id;
        this.userId = userId;
        this.groupId = groupId;
        this.jid = jid;
        this.type = type;
        this.isFrom = isFrom;
        this.isRead = isRead;
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

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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

    public String getIsFrom() {
        return this.isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
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
