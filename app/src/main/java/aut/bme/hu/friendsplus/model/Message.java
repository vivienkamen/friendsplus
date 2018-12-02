package aut.bme.hu.friendsplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message implements Parcelable {

    public String text;
    public String senderUID;
    public long time;
    public boolean unread;


    public Message() {
        text = "";
        senderUID = "";
        time = 0;
        unread = true;
    }

    public Message(String text, String senderUID) {
        this.text = text;
        this.senderUID = senderUID;
        time = System.currentTimeMillis();
        unread = true;

    }

    public Message(Parcel in) {
        text = in.readString();
        senderUID = in.readString();
        time = in.readLong();
        unread = in.readInt() != 0;
    }

    public static final Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(text);
        parcel.writeString(senderUID);
        parcel.writeLong(time);
        parcel.writeInt(unread ? 1 : 0);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("text", text);
        result.put("senderUID", senderUID);
        result.put("time", time);
        result.put("unread", unread);

        return result;
    }
}
