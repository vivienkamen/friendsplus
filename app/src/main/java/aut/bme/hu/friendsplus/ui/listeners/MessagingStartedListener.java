package aut.bme.hu.friendsplus.ui.listeners;

import aut.bme.hu.friendsplus.model.Message;

public interface MessagingStartedListener {
    void onMessagingStarted(Message message, String friendUID);
}
