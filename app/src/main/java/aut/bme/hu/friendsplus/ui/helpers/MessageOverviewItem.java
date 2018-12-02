package aut.bme.hu.friendsplus.ui.helpers;

import aut.bme.hu.friendsplus.model.Message;
import aut.bme.hu.friendsplus.model.User;

public class MessageOverviewItem {
    public User user;
    public Message lastMessage;
    public int unreadMessageCount;

    public MessageOverviewItem() {
        user = new User();
        lastMessage = new Message();
        unreadMessageCount = 0;
    }
}
