package aut.bme.hu.friendsplus.ui.listeners;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.model.Meeting;

public interface TrackedMeetingsListener {
    void onTrackedMeetingsListFound(ArrayList<Meeting> trackedMeetings);
}
