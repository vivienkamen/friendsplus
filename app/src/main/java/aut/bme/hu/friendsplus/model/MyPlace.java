package aut.bme.hu.friendsplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MyPlace implements Parcelable {

    public String id;
    public String address;
    public String name;
    public double latitude;
    public double longitude;


    public MyPlace() {}

    public MyPlace(String id, CharSequence address, CharSequence name,
                   double lat, double lng) {
        this.id = id;
        this.name = name.toString();
        this.address = address.toString();
        this.latitude = lat;
        this.longitude = lng;

    }


    protected MyPlace(Parcel in) {
        id = in.readString();
        address = in.readString();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<MyPlace> CREATOR = new Parcelable.Creator<MyPlace>() {
        @Override
        public MyPlace createFromParcel(Parcel in) {
            return new MyPlace(in);
        }

        @Override
        public MyPlace[] newArray(int size) {
            return new MyPlace[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(address);
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("address", address);
        result.put("name", name);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
