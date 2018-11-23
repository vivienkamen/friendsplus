package aut.bme.hu.friendsplus.ui.addMeeting;

import aut.bme.hu.friendsplus.model.Meeting;

public interface NewMeetingScreen {
    String getName();
    void showValidDate(String date);
    void showValidTime(String time);
}
