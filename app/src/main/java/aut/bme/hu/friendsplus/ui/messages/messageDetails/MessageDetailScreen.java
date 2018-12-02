package aut.bme.hu.friendsplus.ui.messages.messageDetails;

import android.view.View;

import com.firebase.ui.database.FirebaseListAdapter;

import aut.bme.hu.friendsplus.model.Message;

public interface MessageDetailScreen {
    void setMyMessageLayout(View view, String messageText);
    void setFriendMessageLayout(View view, String messageText);
    void setAdapter(FirebaseListAdapter<Message> adapter);
}
