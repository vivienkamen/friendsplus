package aut.bme.hu.friendsplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Meeting implements Parcelable {

    public String name;
    public MyPlace place;
    public long meetingDate;        //in millies
    public long creationTime;       //in millies
    public String uid;
    public String key;
    public int position;
    public boolean tracked = false;
    public boolean finished;
    public HashMap<String, String> trackedFriends = new HashMap<>();

    public Meeting() {}

    public Meeting(String name, MyPlace place, long mDate,
                   long creationTime, String uid, String key) {
        this.name = name;
        this.place = place;
        this.meetingDate = mDate;
        this.creationTime = creationTime;
        this.uid = uid;
        this.key = key;
        this.position = -1;
        tracked = false;
        finished = false;
    }


    public Meeting(Parcel in) {
        name = in.readString();
        place = in.readParcelable(MyPlace.class.getClassLoader());
        meetingDate = in.readLong();
        creationTime = in.readLong();
        uid = in.readString();
        key = in.readString();
        position = in.readInt();
        tracked = in.readInt() != 0;
        finished = in.readInt() != 0;
        trackedFriends = in.readHashMap(String.class.getClassLoader());
    }

    public static final Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(place, i);
        parcel.writeLong(meetingDate);
        parcel.writeLong(creationTime);
        parcel.writeString(uid);
        parcel.writeString(key);
        parcel.writeInt(position);
        parcel.writeInt(tracked ? 1 : 0);
        parcel.writeInt(finished ? 1 : 0);
        parcel.writeMap(trackedFriends);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        Map<String, Object> placeValues = place.toMap();

        result.put("name", name);
        result.put("place", placeValues);
        result.put("meetingDate", meetingDate);
        result.put("creationTime", creationTime);
        result.put("uid", uid);
        result.put("key", key);
        result.put("position", position);
        result.put("tracked", tracked);
        result.put("finished", finished);
        result.put("trackedFriends", trackedFriends);

        return result;
    }

    public void addFriend(String uid) {
        trackedFriends.put(uid, "tracked");
    }

    public void friendArrived(String uid) {
        trackedFriends.remove(uid);
        trackedFriends.put(uid, "arrived");
    }

    public boolean isEverybodyArrived() {
        boolean arrived = true;

        for(Map.Entry<String, String> entry : trackedFriends.entrySet()) {
            if(entry.getValue().equals("tracked")) {
                arrived = false;
            }
        }

        return arrived;

    }

    public List<String> getArrivedFriends() {
        List<String> arrived = new ArrayList<>();

        for(Map.Entry<String, String> entry : trackedFriends.entrySet()) {
            if(entry.getValue().equals("arrived")) {
                arrived.add(entry.getKey());
            }
        }

        return arrived;

    }

    public boolean isFriendInStatus(String uid, String status) {
        boolean friendTracked = false;

        String status2 = trackedFriends.get(uid);

        if(status2 != null && status2.equals(status)) {
            friendTracked = true;
        }

        return friendTracked;
    }

    public boolean containsFriend(String uid) {
        return trackedFriends.containsKey(uid);
    }
}
