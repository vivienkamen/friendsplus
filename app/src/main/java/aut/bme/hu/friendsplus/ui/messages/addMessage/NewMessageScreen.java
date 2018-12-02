package aut.bme.hu.friendsplus.ui.messages.addMessage;

import java.util.ArrayList;

import aut.bme.hu.friendsplus.model.User;

public interface NewMessageScreen {
    String getMessageText();
    void setSendToTextView(ArrayList<String> friends);
}
