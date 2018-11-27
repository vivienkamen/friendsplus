package aut.bme.hu.friendsplus.ui.tracking;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.FriendsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.LocationDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.MeetingsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.MyLocation;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.helpers.FriendsProvider;
import aut.bme.hu.friendsplus.ui.listeners.FriendsReadyListener;
import aut.bme.hu.friendsplus.ui.listeners.LocationListener;
import aut.bme.hu.friendsplus.ui.listeners.TrackedMeetingsListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;
import aut.bme.hu.friendsplus.ui.meetingDetails.MeetingDetailScreen;

public class TrackingPresenter extends Presenter<TrackingScreen> implements LocationListener,
        FriendsReadyListener, UsersListener, TrackedMeetingsListener {

    private static final String TAG = "TrackingPresenter";

    Meeting meeting;
    String myUID;
    LatLng placeLocation;
    private HashMap<String, Marker> markers;
    AuthInteractor authInteractor;
    LocationDatabaseInteractor locationDatabaseInteractor;
    MeetingsDatabaseInteractor meetingsDatabaseInteractor;
    UserDatabaseInteractor userDatabaseInteractor;
    FriendsProvider friends;

    public TrackingPresenter() {
        authInteractor = new AuthInteractor();
        locationDatabaseInteractor = new LocationDatabaseInteractor();
        meetingsDatabaseInteractor = new MeetingsDatabaseInteractor();
        userDatabaseInteractor = new UserDatabaseInteractor();

        locationDatabaseInteractor.setLocationListener(this);
        userDatabaseInteractor.setUsersListener(this);
        meetingsDatabaseInteractor.setTrackedMeetingsListener(this);

        friends = new FriendsProvider(this);
        myUID = authInteractor.getCurrentUser().getUid();
        markers = new HashMap<>();

    }

    @Override
    public void attachScreen(TrackingScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {

        if(meeting != null) {
            this.meeting = meeting;
            if(meeting.containsFriend(myUID)) {
                if(meeting.isFriendInStatus(myUID, "tracked"))
                    screen.setDoneButton();

            } else {

                screen.showAlertDialog("Your friends are already on their way. \nStart your tracking too!");
            }
            screen.setTrackingUI(meeting);
            screen.addMeetingMarker(meeting);
            locationDatabaseInteractor.addChildEventListener();

        } else {
            meetingsDatabaseInteractor.getTrackedMeetings();
        }
    }

    @Override
    public void onTrackedMeetingsListFound(ArrayList<Meeting> trackedMeetings) {

        if(trackedMeetings.isEmpty()) {
            screen.setNoTrackingUI();
        } else {
            screen.showMeetingPickerDialog(trackedMeetings);
        }
    }

    @Override
    public void onLocationAddedOrChanged(DataSnapshot dataSnapshot) {

        String uid = dataSnapshot.getKey();

        if(friends.isFriend(uid)) {
            changeMarkers(dataSnapshot);
        }

    }

    public void changeMarkers(DataSnapshot dataSnapshot) {

        MyLocation lastLocation = new MyLocation();
        lastLocation.timeStamp = 0;

        for (DataSnapshot locationSnapshot: dataSnapshot.getChildren()) {
            MyLocation ml = locationSnapshot.getValue(MyLocation.class);
            if(ml.timeStamp > lastLocation.timeStamp) {
                lastLocation = ml;
            }
        }

        placeLocation = new LatLng(lastLocation.latitude, lastLocation.longitude);

        Log.e(TAG, "changeMarkers: " + placeLocation.toString());

        userDatabaseInteractor.getUserByUid(dataSnapshot.getKey());

    }

    public void setArrival() {
        meeting.friendArrived(myUID);
        if(meeting.isEverybodyArrived()) {
            meeting.tracked = false;
            meeting.finished = true;
            locationDatabaseInteractor.removeLocations();
        }
        updateMeetingDatabase();
    }

    public void updateMeetingDatabase() {
        Map<String, Object> meetingValues = meeting.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/meetings/" + meeting.key, meetingValues);
        meetingsDatabaseInteractor.updateDatabase(childUpdates);
    }

    public void signOut() {
        authInteractor.signOut();
    }

    public void cleanUpLocationListener() {
        locationDatabaseInteractor.cleanUpListener();
    }


    @Override
    public void onFriendsReady() {}

    @Override
    public void onUserFound(User user) {
        if (!markers.containsKey(user.uid)) {
            Marker marker = screen.addMarkerToMap(placeLocation, user.username);
            markers.put(user.uid, marker);
            screen.setMapCamera(placeLocation);

        } else {
            Marker marker = markers.get(user.uid);
            marker.setPosition(placeLocation);
            marker.setTitle(user.username);

            markers.remove(user.uid);
            markers.put(user.uid, marker);

        }
    }

    @Override
    public void onUserNotFound() {}


}
