package aut.bme.hu.friendsplus.ui.listeners;

import java.util.List;

public interface FriendsListener {
    void onFriendListReady(List<String> friends);
    void onFriendAdded(String uid);
    void onFiendRemoved(String uid);
}
