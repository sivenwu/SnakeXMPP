package cn.snake.dbkit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenyk on 2016/12/15.
 */
@Entity
public class ChatInfoModel {
    public String jid;
    public String type;
    public String to;
    public String from;
    public String body;
    public String thread;
    public String state;
    @Generated(hash = 1678207570)
    public ChatInfoModel(String jid, String type, String to, String from,
            String body, String thread, String state) {
        this.jid = jid;
        this.type = type;
        this.to = to;
        this.from = from;
        this.body = body;
        this.thread = thread;
        this.state = state;
    }
    @Generated(hash = 1033339016)
    public ChatInfoModel() {
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
    public String getBody() {
        return this.body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getThread() {
        return this.thread;
    }
    public void setThread(String thread) {
        this.thread = thread;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
