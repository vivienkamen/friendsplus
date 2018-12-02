package aut.bme.hu.friendsplus.ui.listeners;

import aut.bme.hu.friendsplus.model.Message;

public interface LastMessageListener {
    void onLastMessageFound(Message message, String friendUID);
}
