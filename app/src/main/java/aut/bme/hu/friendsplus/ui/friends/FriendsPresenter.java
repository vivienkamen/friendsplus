package aut.bme.hu.friendsplus.ui.friends;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.FriendsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.FriendsListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;
import aut.bme.hu.friendsplus.ui.main.MeetingRowScreen;

public class FriendsPresenter extends Presenter<FriendRowScreen> implements FriendsListener, UsersListener {

    private FriendsDatabaseInteractor friendsDatabaseInteractor;
    private UserDatabaseInteractor userDatabaseInteractor;
    private AuthInteractor authInteractor;

    private ItemChangeListener listener;

    private List<String> friends;

    public FriendsPresenter() {
        friendsDatabaseInteractor = new FriendsDatabaseInteractor();
        userDatabaseInteractor = new UserDatabaseInteractor();
        authInteractor = new AuthInteractor();

        friendsDatabaseInteractor.setFriendsListener(this);
        userDatabaseInteractor.setUsersListener(this);

        friends = new ArrayList<>();

        friendsDatabaseInteractor.addChildEventListener();
    }

    @Override
    public void attachScreen(FriendRowScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void onBindFriendRowViewAtPosition(int position) {
        String uid = friends.get(position);
        userDatabaseInteractor.getUserByUid(uid);
    }

    public int getFriendRowsCount() {
        return friends.size();
    }

    public void setListener(ItemChangeListener listener) {
        this.listener = listener;
    }

    public void signOut() {
        authInteractor.signOut();
    }

    @Override
    public void onFriendListReady(List<String> friends) {}

    @Override
    public void onFriendAdded(String uid) {
        friends.add(uid);
        listener.onItemChanged(friends.size() - 1);
    }

    @Override
    public void onFiendRemoved(String uid) {
        int friendIndex = friends.indexOf(uid);
        if (friendIndex > -1) {

            friends.remove(friendIndex);

            listener.onItemChanged(friendIndex);
        }
    }


    @Override
    public void onUserFound(User user) {
        screen.setFriend(user);
    }

    @Override
    public void onUserNotFound() {

    }

    public void cleanupListener() {
        friendsDatabaseInteractor.cleanupListener();
    }

    public void addFriend(String uid) {

        if(friends.contains(uid)) {
            return;
        }

        friendsDatabaseInteractor.addFriend(uid);

    }

    public String removeFriend(int position) {
        String uid = friends.get(position);
        friendsDatabaseInteractor.removeFriend(uid);

        return uid;
    }

    public void restoreFriend(String uid) {
        friendsDatabaseInteractor.restoreFriend(uid);
    }

}
