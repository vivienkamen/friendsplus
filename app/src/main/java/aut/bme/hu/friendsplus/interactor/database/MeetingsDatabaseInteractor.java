package aut.bme.hu.friendsplus.interactor.database;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.ui.listeners.MeetingsListener;
import aut.bme.hu.friendsplus.ui.listeners.TrackedMeetingsListener;
import aut.bme.hu.friendsplus.ui.listeners.TrackingListener;

public class MeetingsDatabaseInteractor {

    private static final String TAG = "MeetingsDbInteractor";

    private DatabaseReference mDatabase;
    private ChildEventListener childEventListener;
    private MeetingsListener meetingsListener;
    private TrackingListener trackingListener;
    private TrackedMeetingsListener trackedMeetingsListener;

    public MeetingsDatabaseInteractor() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setMeetingsListener(MeetingsListener meetingsListener) {
        this.meetingsListener = meetingsListener;
    }

    public void setTrackedMeetingsListener(TrackedMeetingsListener trackedMeetingsListener) {
        this.trackedMeetingsListener = trackedMeetingsListener;
    }

    public void setTrackingListener(TrackingListener trackingListener) {
        this.trackingListener = trackingListener;
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

    public void checkMyTrackingStarted(final String myUID) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean trackingStarted = false;

                for(DataSnapshot meetingSnapshot : dataSnapshot.child("meetings").getChildren()) {
                    Meeting meeting = meetingSnapshot.getValue(Meeting.class);
                    if(meeting.tracked && meeting.containsFriend(myUID)) {
                        trackingStarted = true;
                    }
                }
                trackingListener.onTrackingFound(trackingStarted);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void updateDatabase(Map<String, Object> childUpdates) {

        mDatabase.updateChildren(childUpdates);
    }

    public void getTrackedMeetings() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Meeting> trackedMeetings = new ArrayList<>();
                for(DataSnapshot meetingDataSnapshot : dataSnapshot.child("meetings").getChildren()) {
                    Meeting meeting = meetingDataSnapshot.getValue(Meeting.class);
                    if(meeting.tracked) {

                        HashMap<String, String> tf = new HashMap<>();
                        for(DataSnapshot mapDataSnapshot : meetingDataSnapshot.child("trackedFriends").getChildren()) {
                            tf.put(mapDataSnapshot.getKey(), mapDataSnapshot.getValue().toString());
                        }
                        meeting.trackedFriends = tf;
                        trackedMeetings.add(meeting);
                    }
                }
                trackedMeetingsListener.onTrackedMeetingsListFound(trackedMeetings);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
