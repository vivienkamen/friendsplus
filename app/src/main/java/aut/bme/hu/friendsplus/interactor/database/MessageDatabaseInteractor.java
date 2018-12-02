package aut.bme.hu.friendsplus.interactor.database;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.model.Message;
import aut.bme.hu.friendsplus.ui.listeners.FriendsListener;
import aut.bme.hu.friendsplus.ui.listeners.LastMessageListener;
import aut.bme.hu.friendsplus.ui.listeners.UnreadMessageListener;

public class MessageDatabaseInteractor {

    private static final String TAG = "MessageDBInteractor";

    private DatabaseReference mDatabase;
    private ChildEventListener friendsChildEventListener;
    private FriendsListener friendsListener;
    private UnreadMessageListener unreadMessageListener;
    private LastMessageListener lastMessageListener;

    public MessageDatabaseInteractor() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setFriendsListener(FriendsListener friendsListener) {
        this.friendsListener = friendsListener;
    }

    public void setUnreadMessageListener(UnreadMessageListener unreadMessageListener) {
        this.unreadMessageListener = unreadMessageListener;
    }

    public void setLastMessageListener(LastMessageListener lastMessageListener) {
        this.lastMessageListener = lastMessageListener;
    }

    public void addMessage(Message message, String myUID, String friendUID) {

        String key = mDatabase.child("messages").child(myUID).child(friendUID).push().getKey();
        mDatabase.child("messages").child(myUID).child(friendUID).child(key).setValue(message);
    }

    public void removeMessagesFromFriend(String myUID, String friendUID) {
        mDatabase.child("messages").child(myUID).child(friendUID).removeValue();
    }

    public void getUnreadMessagesCount(String myUID, final String friendUID) {

        mDatabase.child("messages").child(myUID).child(friendUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int msgCount = 0;
                for(DataSnapshot messageDataSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageDataSnapshot.getValue(Message.class);
                    if(message.unread) {
                        msgCount++;
                    }
                }
                unreadMessageListener.onCountFound(msgCount, friendUID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getLastMessage(String myUID, final String friendUID) {
        mDatabase.child("messages").child(myUID).child(friendUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message lastMessage = new Message();
                for(DataSnapshot messageDataSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageDataSnapshot.getValue(Message.class);

                    if(lastMessage.time < message.time) {
                        lastMessage = message;
                    }
                }
                lastMessageListener.onLastMessageFound(lastMessage, friendUID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void addFriendsChildEventListener(String myUID) {
        friendsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                String uid = dataSnapshot.getKey();
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

                String uid = dataSnapshot.getKey();
                friendsListener.onFiendRemoved(uid);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "friends:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("messages/" + myUID).addChildEventListener(friendsChildEventListener);

    }

    public void cleanupListeners(String myUID) {
        if (friendsChildEventListener != null) {
            mDatabase.child("messages/" + myUID).removeEventListener(friendsChildEventListener);
        }
    }

    public DatabaseReference getReference(String myUID, String friendUID) {
        return mDatabase.child("messages").child(myUID).child(friendUID);
    }

}
