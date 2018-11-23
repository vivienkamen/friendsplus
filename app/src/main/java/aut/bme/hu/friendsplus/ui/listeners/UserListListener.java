package aut.bme.hu.friendsplus.ui.listeners;

import java.util.List;

import aut.bme.hu.friendsplus.model.User;

public interface UserListListener {
    void onUserListReady(List<User> users);
}
