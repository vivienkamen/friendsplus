package aut.bme.hu.friendsplus.ui.helpers;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;

public class MeetingItem {
    Meeting meeting;
    User user;
    String key;

    public MeetingItem() {
        meeting = new Meeting();
        user = new User();
        key = "";
    }
}
