package aut.bme.hu.friendsplus.ui.tracking;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import aut.bme.hu.friendsplus.model.Meeting;

public interface TrackingScreen {
    void setTrackingUI(Meeting meeting);
    void setNoTrackingUI();
    void setStartButton();
    void setDoneButton();
    void showAlertDialog(String message);
    void showMeetingPickerDialog(ArrayList<Meeting> meetingList);
    Marker addMarkerToMap(LatLng placeLocation, String username);
    void setMapCamera(LatLng placeLocation);
    void addMeetingMarker(Meeting meeting);
}
