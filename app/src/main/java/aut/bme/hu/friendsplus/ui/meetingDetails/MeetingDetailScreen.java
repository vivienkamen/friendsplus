package aut.bme.hu.friendsplus.ui.meetingDetails;

import aut.bme.hu.friendsplus.model.Meeting;

public interface MeetingDetailScreen {
    void refreshUI(Meeting meeting);
    void setAddedByTextView(String creator);
    void setStartButtonFinished();
    void setStartButtonNoTracking();
    void setStartButtonThisTrackingInProgress(boolean buttonEnabled);
    void setStartButtonOtherTrackingInProgress();
    void setStartButtonExpired();
}
