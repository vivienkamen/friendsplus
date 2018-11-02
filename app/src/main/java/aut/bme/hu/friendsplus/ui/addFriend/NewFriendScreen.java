package aut.bme.hu.friendsplus.ui.addFriend;

import aut.bme.hu.friendsplus.model.User;

public interface NewFriendScreen {
    void updateUI();
    void setImage();
    void setUser(User user);
    void setError();
}
