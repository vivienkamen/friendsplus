package aut.bme.hu.friendsplus.ui.helpers;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.database.FriendsDatabaseInteractor;
import aut.bme.hu.friendsplus.ui.listeners.FriendsListener;
import aut.bme.hu.friendsplus.ui.listeners.FriendsReadyListener;

public class FriendsProvider implements FriendsListener {
    FriendsDatabaseInteractor friendsDatabaseInteractor;
    List<String> friends;
    FriendsReadyListener listener;

    public FriendsProvider(FriendsReadyListener listener) {
        friendsDatabaseInteractor = new FriendsDatabaseInteractor();
        friendsDatabaseInteractor.setFriendsListener(this);
        friends = new ArrayList<>();

        this.listener = listener;

        friendsDatabaseInteractor.getFriends();
    }

    @Override
    public void onFriendListReady(List<String> friends) {
        this.friends = friends;
        listener.onFriendsReady();
    }

    @Override
    public void onFriendAdded(String uid) {}

    @Override
    public void onFiendRemoved(String uid) {}

    public boolean isFriend(String uid) {
        if(friends.contains(uid)) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getFriendsList() {
        return (ArrayList<String>) friends;
    }

}
