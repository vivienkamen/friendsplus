package aut.bme.hu.friendsplus;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.ui.meetings.meetingDetails.MeetingDetailPresenter;
import aut.bme.hu.friendsplus.ui.meetings.meetingDetails.MeetingDetailScreen;

public class MeetingDetailPresenterTest {

    MeetingDetailPresenter presenter;
    MeetingDetailScreen screen;
    String result = "";


    @Before
    public void setUp() {
        presenter = new MeetingDetailPresenter();
        screen = new MeetingDetailScreen() {
            @Override
            public void refreshUI(Meeting meeting) {}

            @Override
            public void setAddedByTextView(String creator) {}

            @Override
            public void setStartButtonFinished() {
                result = "finished";
            }

            @Override
            public void setStartButtonNoTracking() {
                result = "noTracking";
            }

            @Override
            public void setStartButtonThisTrackingInProgress(boolean buttonEnabled) {
                if(buttonEnabled) {
                    result = "myTrackingNotStarted";
                } else  {
                    result = "myTrackingStartedNotHere";
                }
            }

            @Override
            public void setStartButtonOtherTrackingInProgress() {
                result = "otherInProgress";
            }

            @Override
            public void setStartButtonExpired() {
                result = "expired";
            }
        };

        presenter.attachScreen(screen);

    }

    @Test
    public void testSetStartButtonFinished() {
        Meeting meeting = new Meeting();
        meeting.finished = true;
        presenter.setMeeting(meeting);
        presenter.setStartButton();
        Assert.assertEquals("finished", result);
    }

    @Test
    public void testSetStartButtonExpired() {
        Meeting meeting = new Meeting();
        meeting.finished = false;
        meeting.meetingDate = 0;
        presenter.setMeeting(meeting);
        presenter.setStartButton();
        Assert.assertEquals("expired", result);
    }

    @Test
    public void testSetStartButtonNoTracking() {
        Meeting meeting = new Meeting();
        meeting.finished = false;
        meeting.tracked = false;
        meeting.meetingDate = System.currentTimeMillis() + 100000;
        presenter.setMeeting(meeting);
        presenter.setStartButton();
        Assert.assertEquals("noTracking", result);
    }

    @Test
    public void testSetStartButtonOtherTrackingInProgress() {
        Meeting meeting = new Meeting();
        meeting.finished = false;
        meeting.tracked = false;
        meeting.meetingDate = System.currentTimeMillis() + 100000;
        presenter.myTrackingStarted = true;
        presenter.setMeeting(meeting);
        presenter.setStartButton();
        Assert.assertEquals("otherInProgress", result);
    }

    @Test
    public void testSetStartButtonThisTrackingInProgressMyTrackingNotStarted() {
        Meeting meeting = new Meeting();
        meeting.finished = false;
        meeting.tracked = true;
        meeting.meetingDate = System.currentTimeMillis() + 100000;
        presenter.myTrackingStarted = false;
        presenter.setMeeting(meeting);
        presenter.setStartButton();
        Assert.assertEquals("myTrackingNotStarted", result);
    }

    @Test
    public void testSetStartButtonThisTrackingInProgressMyTrackingStartedNotHere() {
        Meeting meeting = new Meeting();
        meeting.finished = false;
        meeting.tracked = true;
        meeting.meetingDate = System.currentTimeMillis() + 100000;
        presenter.myTrackingStarted = true;
        presenter.setMeeting(meeting);
        presenter.setStartButton();
        Assert.assertEquals("myTrackingStartedNotHere", result);
    }
}
