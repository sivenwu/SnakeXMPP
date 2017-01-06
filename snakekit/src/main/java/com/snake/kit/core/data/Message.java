package com.snake.kit.core.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yuan on 2017/1/3.
 * Detail simple message in outside
 */

public class Message implements Parcelable{

    private String type; // 与db内部ChatInfoModel类型定义一致
    private String message;

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Message() {
    }

    protected Message(Parcel in) {
        type = in.readString();
        message = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(message);
    }
}
