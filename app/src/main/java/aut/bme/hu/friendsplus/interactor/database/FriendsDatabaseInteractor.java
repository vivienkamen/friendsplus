package aut.bme.hu.friendsplus.interactor.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.ui.listeners.FriendsListener;

public class FriendsDatabaseInteractor {

    private static final String TAG = "FriendsDbInteractor";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ChildEventListener childEventListener;
    private FriendsListener friendsListener;


    public FriendsDatabaseInteractor() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void setFriendsListener(FriendsListener friendsListener) {
        this.friendsListener = friendsListener;
    }

    public void getFriends() {
        mDatabase.child("friends/" + mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> friends = new ArrayList<>();
                for(DataSnapshot friendsDataSnapshot : dataSnapshot.getChildren()) {
                    String uid = (String) friendsDataSnapshot.getValue();
                    friends.add(uid);
                }
                friends.add(mAuth.getUid());
                friendsListener.onFriendListReady(friends);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addChildEventListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                String uid = (String) dataSnapshot.getValue();
                friendsListener.onFriendAdded(uid);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                //nem lehet változtatni, csak törölni
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String uid = (String) dataSnapshot.getValue();
                friendsListener.onFiendRemoved(uid);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "friends:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("friends/" + mAuth.getUid()).addChildEventListener(childEventListener);

    }

    public void cleanupListener() {
        if (childEventListener != null) {
            mDatabase.child("friends/" + mAuth.getUid()).removeEventListener(childEventListener);
        }
    }

    public void addFriend(String uid) {
        mDatabase.child("friends/" + mAuth.getUid()).child(uid).setValue(uid);
    }

    public void removeFriend(String uid) {
        mDatabase.child("friends/" + mAuth.getUid()).child(uid).removeValue();
    }

    public void restoreFriend(String uid) {
        mDatabase.child("friends/" + mAuth.getUid()).child(uid).setValue(uid);
    }
}
