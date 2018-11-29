package aut.bme.hu.friendsplus.ui.messages;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.MessageDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.Message;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.FriendsListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.UnreadMessageListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;
import aut.bme.hu.friendsplus.ui.meetings.MeetingRowScreen;

public class MessagesOverviewPresenter extends Presenter<MessagesOverviewScreen> implements FriendsListener,
        UsersListener, UnreadMessageListener {

    private AuthInteractor authInteractor;
    private UserDatabaseInteractor userDatabaseInteractor;
    private MessageDatabaseInteractor messageDatabaseInteractor;

    private ItemChangeListener listener;
    private ArrayList<User> friends;
    private String myUID;


    public MessagesOverviewPresenter() {

        authInteractor = new AuthInteractor();
        userDatabaseInteractor = new UserDatabaseInteractor();
        messageDatabaseInteractor = new MessageDatabaseInteractor();

        userDatabaseInteractor.setUsersListener(this);
        messageDatabaseInteractor.setFriendsListener(this);
        messageDatabaseInteractor.setUnreadMessageListener(this);

        friends = new ArrayList<>();
        myUID = authInteractor.getCurrentUser().getUid();

        messageDatabaseInteractor.addFriendsChildEventListener(myUID);

    }

    @Override
    public void attachScreen(MessagesOverviewScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void onBindMessageRowViewAtPosition(int position) {
        User friend = friends.get(position);
        screen.setFriend(friend);
    }

    public int getMeetingRowsCount() {
        return friends.size();
    }

    public void setListener(ItemChangeListener listener) {
        this.listener = listener;
    }

    public void addFriendWithNewMessage(Message message, String friendUID) {
        messageDatabaseInteractor.addMessage(message, myUID, friendUID);
    }

    public void removeMessagesFromFriend(int index) {
        User friend = friends.get(index);
        messageDatabaseInteractor.removeMessagesFromFriend(myUID, friend.uid);

    }

    public void signOut() {
        authInteractor.signOut();
    }

    public void cleanupListener() {
        messageDatabaseInteractor.cleanupListeners(myUID);
    }

    @Override
    public void onFriendListReady(List<String> friends) {}

    @Override
    public void onFriendAdded(String friendUID) {
        if(!containsFriend(friendUID)) {
            userDatabaseInteractor.getUserByUid(friendUID);
            messageDatabaseInteractor.getUnreadMessagesCount(myUID,friendUID);
        }
    }

    @Override
    public void onFiendRemoved(String uid) {
        User friend = getFriend(uid);
        if(friend != null) {
            int index = friends.indexOf(friend);
            friends.remove(index);
            listener.onItemChanged(index);
        }

    }

    public boolean containsFriend(String uid) {
        for(User user : friends) {
            if(user.uid.equals(uid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUserFound(User user) {
        friends.add(user);
        listener.onItemChanged(friends.size() - 1);
    }

    @Override
    public void onUserNotFound() {}

    public User getFriend(String uid) {
        for(User user : friends) {
            if(user.uid.equals(uid)) {
                return  user;
            }
        }
        return null;
    }

    @Override
    public void onCountFound(int newMessageCount) {
        screen.setUnreadMessageTextView(newMessageCount);
    }
}
