package aut.bme.hu.friendsplus.interactor.database;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.ui.listeners.MeetingsListener;

public class MeetingsDatabaseInteractor {

    private static final String TAG = "MeetingsDbInteractor";

    private DatabaseReference mDatabase;
    private ChildEventListener childEventListener;
    private MeetingsListener meetingsListener;

    public MeetingsDatabaseInteractor(MeetingsListener listener) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        meetingsListener = listener;
    }

    public void addMeetingsListener() {

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                Meeting meeting = dataSnapshot.getValue(Meeting.class);
                meetingsListener.onMeetingAdded(meeting, dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                Meeting newMeeting = dataSnapshot.getValue(Meeting.class);
                meetingsListener.onMeetingChanged(newMeeting, dataSnapshot.getKey());


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                meetingsListener.onMeetingRemoved(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "meetings:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("meetings").addChildEventListener(childEventListener);

    }

    public void cleanupListener() {
        if (childEventListener != null) {
            mDatabase.child("meetings").removeEventListener(childEventListener);
        }
    }

    public void addMeeting(Meeting meeting) {

        String key = mDatabase.child("meetings").push().getKey();
        meeting.key = key;
        mDatabase.child("meetings").child(key).setValue(meeting);

    }

    public void removeMeeting(String key) {
        mDatabase.child("meetings").child(key).removeValue();

    }

    public void restoreMeeting(Meeting meeting, String key) {
        mDatabase.child("meetings").child(key).setValue(meeting);


    }
}
