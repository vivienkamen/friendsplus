package aut.bme.hu.friendsplus.interactor.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aut.bme.hu.friendsplus.model.MyLocation;
import aut.bme.hu.friendsplus.ui.listeners.LocationListener;

public class LocationDatabaseInteractor {

    private static final String TAG = "LocationDBInteractor";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ChildEventListener childEventListener;
    private LocationListener locationListener;

    public LocationDatabaseInteractor() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public void setLocation(MyLocation location) {
        String key = mDatabase.child("locations/" + mAuth.getUid()).push().getKey();
        mDatabase.child("locations/" + mAuth.getUid()).child(key).setValue(location);
    }

    public void addChildEventListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                locationListener.onLocationAddedOrChanged(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                locationListener.onLocationAddedOrChanged(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "meetings:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("locations").addChildEventListener(childEventListener);
    }

    public void cleanUpListener() {
        if (childEventListener != null) {
            mDatabase.child("locations").removeEventListener(childEventListener);

        }
    }

    public void removeLocations() {
        mDatabase.child("locations").removeValue();
    }

}
