package cn.snake.dbkit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenyk on 2016/12/23.
 * 群成员info
 */
@Entity
public class MemberInfo {
    @Id(autoincrement = true)
    public Long _id;
    public String userId;
    public int groupId;
    public String joinTime;//入群时间
    public String jid;
    public String userName;
    public String headPicUrl;//头像
    public int isGroupMaster;//是否是群主 0 -> no , 1 -> yes
    public String otherInfo;//其他信息
    @Generated(hash = 1762044640)
    public MemberInfo(Long _id, String userId, int groupId, String joinTime,
            String jid, String userName, String headPicUrl, int isGroupMaster,
            String otherInfo) {
        this._id = _id;
        this.userId = userId;
        this.groupId = groupId;
        this.joinTime = joinTime;
        this.jid = jid;
        this.userName = userName;
        this.headPicUrl = headPicUrl;
        this.isGroupMaster = isGroupMaster;
        this.otherInfo = otherInfo;
    }
    @Generated(hash = 175316736)
    public MemberInfo() {
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
    public String getJoinTime() {
        return this.joinTime;
    }
    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
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
    public String getHeadPicUrl() {
        return this.headPicUrl;
    }
    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }
    public int getIsGroupMaster() {
        return this.isGroupMaster;
    }
    public void setIsGroupMaster(int isGroupMaster) {
        this.isGroupMaster = isGroupMaster;
    }
    public String getOtherInfo() {
        return this.otherInfo;
    }
    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

}
