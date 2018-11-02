package aut.bme.hu.friendsplus.ui.listeners;

import aut.bme.hu.friendsplus.model.Meeting;

public interface MeetingsListener {
    void onMeetingAdded(Meeting meeting, String key);
    void onMeetingChanged(Meeting meeting, String key);
    void onMeetingRemoved(String key);
}
