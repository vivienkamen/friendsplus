package aut.bme.hu.friendsplus.ui.addMeeting;

import aut.bme.hu.friendsplus.model.Meeting;

public interface NewMeetingScreen {
    boolean isValid();
    Meeting getMeeting();
}
