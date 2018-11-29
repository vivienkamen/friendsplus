package aut.bme.hu.friendsplus.ui.messages;

import aut.bme.hu.friendsplus.model.User;

public interface MessagesOverviewScreen {
    void setFriend(User friend);
    void setUnreadMessageTextView(int unreadMessageCount);
}
