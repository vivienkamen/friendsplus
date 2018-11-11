package aut.bme.hu.friendsplus.ui.account;

import android.net.Uri;

import aut.bme.hu.friendsplus.model.User;

public interface AccountScreen {
    void setImageUri(String uri);
    Uri getProfileImageUri();
    void setUpdatedUser();
    User getUpdatedUser();
    void setCurrentUser(User user);
    void updateCurrentUser();
    void resetUpdatedUser();
    void showSuccessfulUpdate();
    void initUI();
    void updateUI();
    void signOut();
}
