package aut.bme.hu.friendsplus.ui.listeners;

import com.google.firebase.auth.FirebaseUser;

public interface AuthListener {
    public void onSuccess(String message);
    public void onFailure(String message);
}
