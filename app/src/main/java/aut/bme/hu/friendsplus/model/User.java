package aut.bme.hu.friendsplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Parcelable {

    public String username;
    public String email;
    public String uid;
    public String imageUri;

    public User() {}

    public User(String username, String email, String uid) {
        this.username = username;
        this.email = email;
        this.uid = uid;
    }

    public User(Parcel in) {
        username = in.readString();
        email = in.readString();
        uid = in.readString();
        imageUri = in.readString();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("username", username);
        result.put("email", email);
        result.put("uid", uid);
        result.put("imageUri", imageUri);

        return result;
    }

    public static final Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(uid);
        parcel.writeString(imageUri);
    }
}
