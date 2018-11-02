package aut.bme.hu.friendsplus.ui.main;

import android.content.Context;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;

public interface MeetingRowScreen {
    void setMeeting(Meeting meeting);
    void setImage(User user);
}
