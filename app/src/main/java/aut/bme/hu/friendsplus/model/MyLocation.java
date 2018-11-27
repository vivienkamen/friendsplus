package aut.bme.hu.friendsplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MyLocation implements Parcelable {

    public double latitude;
    public double longitude;
    public long timeStamp;

    public MyLocation() {}


    protected MyLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        timeStamp = in.readLong();
    }

    public static final Creator<MyLocation> CREATOR = new Creator<MyLocation>() {
        @Override
        public MyLocation createFromParcel(Parcel in) {
            return new MyLocation(in);
        }

        @Override
        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeLong(timeStamp);
    }
}

