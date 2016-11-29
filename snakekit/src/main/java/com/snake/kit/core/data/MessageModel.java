package com.snake.kit.core.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yuan on 2016/11/14.
 * Detail 组装内部消息实体类
 */

public class MessageModel implements Parcelable{

    private String id;
    private String type;
    private String to;
    private String from;
    private String body;
    private String thread;
    private String state;

    public MessageModel() {
    }

    public MessageModel(String id, String type, String to, String from, String body, String thread, String state) {
        this.id = id;
        this.type = type;
        this.to = to;
        this.from = from;
        this.body = body;
        this.thread = thread;
        this.state = state;
    }

    protected MessageModel(Parcel in) {
        id = in.readString();
        type = in.readString();
        to = in.readString();
        from = in.readString();
        body = in.readString();
        thread = in.readString();
        state = in.readString();
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(to);
        dest.writeString(from);
        dest.writeString(body);
        dest.writeString(thread);
        dest.writeString(state);
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", body='" + body + '\'' +
                ", thread='" + thread + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
