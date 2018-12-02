package aut.bme.hu.friendsplus.ui.messages.messageDetails;

import android.view.View;

import com.firebase.ui.database.FirebaseListAdapter;

import java.util.HashMap;
import java.util.Map;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.MessageDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Message;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.Presenter;

public class MessageDetailPresenter extends Presenter<MessageDetailScreen> {

    MessageDatabaseInteractor messageDatabaseInteractor;
    AuthInteractor authInteractor;
    FirebaseListAdapter<Message> adapter;
    User friend;
    String myUID;

    public MessageDetailPresenter(User user) {
        messageDatabaseInteractor = new MessageDatabaseInteractor();
        authInteractor = new AuthInteractor();
        friend = user;
        myUID = authInteractor.getCurrentUser().getUid();

    }

    @Override
    public void attachScreen(MessageDetailScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void setAdapter(BaseActivity activity) {
        adapter = new FirebaseListAdapter<Message>(activity, Message.class,
                R.layout.item_message, messageDatabaseInteractor.getReference(myUID, friend.uid)) {
            @Override
            protected void populateView(View view, Message message, int position) {

                if(message.senderUID.equals(myUID)) {
                    screen.setMyMessageLayout(view, message.text);
                } else {
                    screen.setFriendMessageLayout(view, message.text);
                }
                if(message.unread) {
                    updateUnreadMessage(message);
                }
            }
        };

        screen.setAdapter(adapter);
    }

    public void sendMessage(String messageText) {
        Message message = new Message(messageText, myUID);

        messageDatabaseInteractor.addMessage(message, friend.uid, myUID);
        message.unread = false;
        messageDatabaseInteractor.addMessage(message, myUID, friend.uid);
    }

    public User getFriend() {
        return friend;
    }

    public void updateUnreadMessage(Message message) {
        message.unread = false;
        Map<String, Object> messageValues = message.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/messages/" + myUID + "/" + friend.uid + "/" + message.key, messageValues);

        messageDatabaseInteractor.updateDatabase(childUpdates);
    }
}
