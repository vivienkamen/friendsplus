package aut.bme.hu.friendsplus.ui.addFriend;

import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class NewFriendPresenter extends Presenter<NewFriendScreen> implements UsersListener {

    UserDatabaseInteractor userDatabaseInteractor;

    public NewFriendPresenter() {
        userDatabaseInteractor = new UserDatabaseInteractor(this, null);
    }

    @Override
    public void attachScreen(NewFriendScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void searchUser(String email) {

        userDatabaseInteractor.getUserByEmail(email);
    }

    @Override
    public void onUserFound(User user) {
        screen.setUser(user);
        screen.updateUI();
    }

    @Override
    public void onUserNotFound() {
        screen.setError();
    }
}
