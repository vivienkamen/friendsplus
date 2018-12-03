package aut.bme.hu.friendsplus.ui.messages.addMessage;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Message;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.util.FriendsProvider;
import aut.bme.hu.friendsplus.ui.listeners.FriendsReadyListener;
import aut.bme.hu.friendsplus.ui.listeners.UserListListener;

public class NewMessagePresenter extends Presenter<NewMessageScreen> implements UserListListener, FriendsReadyListener {

    FriendsProvider friendsProvider;
    UserDatabaseInteractor userDatabaseInteractor;
    AuthInteractor authInteractor;

    List<User> users;
    User selectedFriend;

    public NewMessagePresenter() {

        users = new ArrayList<>();
        selectedFriend = new User();

        friendsProvider = new FriendsProvider(this);
        userDatabaseInteractor = new UserDatabaseInteractor();
        userDatabaseInteractor.setUserListListener(this);
        authInteractor = new AuthInteractor();
    }

    @Override
    public void attachScreen(NewMessageScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    @Override
    public void onUserListReady(List<User> users) {
        this.users = users;
        screen.setSendToTextView(getNamesList(users));
    }

    private ArrayList<String> getNamesList(List<User> users) {
        ArrayList<String> names = new ArrayList<>();

        for(User user : users) {
            names.add(user.username);
        }

        return names;
    }

    @Override
    public void onFriendsReady() {
        userDatabaseInteractor.getUsers(friendsProvider.getFriendsList());
    }

    public Message getMessage() {
        String myUID = authInteractor.getCurrentUser().getUid();
        return new Message(screen.getMessageText(),myUID);
    }

    public String getFriendUID() {

        return selectedFriend.uid;
    }

    public void setSelectedFriend(String selectedFriendName) {
        selectedFriend = getFriendFromName(selectedFriendName);
    }

    private User getFriendFromName(String name) {
        for(User user : users) {
            if(user.username.equals(name)) {
                return user;
            }
        }
        return null;
    }
}
