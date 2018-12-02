package aut.bme.hu.friendsplus.ui.friends;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.FriendsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.FriendsListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.UserListListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class FriendsPresenter extends Presenter<FriendRowScreen> implements FriendsListener, UsersListener {

    private FriendsDatabaseInteractor friendsDatabaseInteractor;
    private UserDatabaseInteractor userDatabaseInteractor;
    private AuthInteractor authInteractor;

    private ItemChangeListener itemChangeListener;

    private List<User> friends;

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
        User user = friends.get(position);
        screen.setFriend(user);

    }

    public int getFriendRowsCount() {
        return friends.size();
    }

    public void setItemChangeListener(ItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    public void signOut() {
        authInteractor.signOut();
    }

    @Override
    public void onFriendListReady(List<String> friends) {}

    @Override
    public void onFriendAdded(String uid) {

        userDatabaseInteractor.getUserByUid(uid);

    }

    @Override
    public void onFiendRemoved(String uid) {

        int friendIndex = friends.indexOf(getFriendFromUID(uid));
        if (friendIndex > -1) {

            friends.remove(friendIndex);

            itemChangeListener.onItemRemoved(friendIndex);
        }
    }


    @Override
    public void onUserFound(User user) {
        friends.add(user);
        itemChangeListener.onItemInserted(friends.size() - 1);
    }

    @Override
    public void onUserNotFound() {}

    private User getFriendFromUID(String uid) {
        User friend = new User();
        for (User user : friends) {
            if(user.uid.equals(uid)) {
                friend = user;
            }
        }
        return friend;
    }

    public void cleanupListener() {
        friendsDatabaseInteractor.cleanupListener();
    }

    public void addFriend(String uid) {

        if(friends.contains(getFriendFromUID(uid))) {
            return;
        }

        friendsDatabaseInteractor.addFriend(uid);
    }

    public String removeFriend(int position) {
        User friend = friends.get(position);
        friendsDatabaseInteractor.removeFriend(friend.uid);

        return friend.uid;
    }

    public void restoreFriend(String uid) {
        friendsDatabaseInteractor.restoreFriend(uid);
    }


}
