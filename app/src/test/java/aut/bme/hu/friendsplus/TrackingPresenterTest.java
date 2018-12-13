package aut.bme.hu.friendsplus;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.ui.tracking.TrackingPresenter;
import aut.bme.hu.friendsplus.ui.tracking.TrackingScreen;

public class TrackingPresenterTest {

    TrackingPresenter presenter;
    String result;

    @Before
    public void setUp() {
        presenter = new TrackingPresenter();

        TrackingScreen screen = new TrackingScreen() {
            @Override
            public void setTrackingUI(Meeting meeting) {}

            @Override
            public void setNoTrackingUI() {
                result = "noTracking";
            }

            @Override
            public void setStartButton() {

            }

            @Override
            public void setDoneButton() {
                result = "done";
            }

            @Override
            public void showAlertDialog(String message) {
                result = "alert";
            }

            @Override
            public void showMeetingPickerDialog(ArrayList<Meeting> meetingList) {
                result = "meetingPicker";
            }

            @Override
            public Marker addMarkerToMap(LatLng placeLocation, String username) {
                return null;
            }

            @Override
            public void setMapCamera(LatLng placeLocation) {

            }

            @Override
            public void addMeetingMarker(Meeting meeting) {}
        };
        presenter.attachScreen(screen);
    }

    @Test
    public void testSetMeetingContainsMyUID() {
        Meeting meeting = new Meeting();
        meeting.addFriend("test");
        presenter.myUID = "test";
        presenter.setMeeting(meeting);
        Assert.assertEquals("done", result);
    }

    @Test
    public void testSetMeetingNotContainsMyUID() {
        Meeting meeting = new Meeting();
        presenter.myUID = "test";
        presenter.setMeeting(meeting);
        Assert.assertEquals("alert", result);
    }

    @Test
    public void testOnTrackedMeetingsListFoundEmpty() {
        ArrayList<Meeting> trackedMeetings = new ArrayList<>();

        presenter.onTrackedMeetingsListFound(trackedMeetings);
        Assert.assertEquals("noTracking", result);
    }

    @Test
    public void testOnTrackedMeetingsListFoundNotEmpty() {
        ArrayList<Meeting> trackedMeetings = new ArrayList<>();
        trackedMeetings.add(new Meeting());

        presenter.onTrackedMeetingsListFound(trackedMeetings);
        Assert.assertEquals("meetingPicker", result);
    }
}
