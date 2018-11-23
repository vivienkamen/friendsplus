package aut.bme.hu.friendsplus.ui.meetingDetails;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.MeetingsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.TrackingListener;
import aut.bme.hu.friendsplus.ui.listeners.UserListListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class MeetingDetailPresenter extends Presenter<MeetingDetailScreen> implements TrackingListener,
        UsersListener, UserListListener {

    AuthInteractor authInteractor;
    MeetingsDatabaseInteractor meetingsDatabaseInteractor;
    UserDatabaseInteractor userDatabaseInteractor;

    Meeting meeting;
    String myUID;
    List<User> users;
    private String meetingCreator;
    private boolean trackingStarted;

    public MeetingDetailPresenter(Meeting meeting) {
        authInteractor = new AuthInteractor(null, null);
        meetingsDatabaseInteractor = new MeetingsDatabaseInteractor(null, this);
        userDatabaseInteractor = new UserDatabaseInteractor(this, this);

        this.meeting = meeting;
        myUID = authInteractor.getCurrentUser().getUid();
        trackingStarted = false;
        users = new ArrayList<>();

        if(meeting.position < 0) throw new RuntimeException("Érvénytelen pozíció!");

        meetingsDatabaseInteractor.checkTrackingStarted();
        userDatabaseInteractor.getUsers(meeting.getArrivedFriends());
    }

    @Override
    public void attachScreen(MeetingDetailScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public boolean isMyMeeting() {
        return myUID.equals(meeting.uid);
    }

    public void signOut() {
        authInteractor.signOut();
    }

    @Override
    public void onTrackingFound(boolean trackingStarted) {
        this.trackingStarted = trackingStarted;
        screen.refreshUI(meeting);

        getUserName();
    }

    public void getUserName() {
        userDatabaseInteractor.getUserByUid(myUID);
    }

    @Override
    public void onUserFound(User user) {
        meetingCreator = user.username;
        screen.setAddedByTextView(meetingCreator);
    }

    @Override
    public void onUserNotFound() {

    }

    public void setStartButton() {
        if(meeting.finished) {
            screen.setStartButtonFinished();
        } else {

            if((!trackingStarted && !meeting.tracked)) {

                screen.setStartButtonNoTracking();

            }else if(trackingStarted && meeting.tracked) {

                screen.setStartButtonThisTrackingInProgress(meeting.containsFriend(myUID));

            } else if(trackingStarted && !meeting.tracked){

                screen.setStartButtonOtherTrackingInProgress();
            }
        }
    }

    public void startTracking() {
        meeting.tracked = true;
        meeting.addFriend(myUID);
        updateMeetingsDatabase();
    }

    public void updateMeetingsDatabase() {
        Map<String, Object> meetingValues = meeting.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/meetings/" + meeting.key, meetingValues);

        meetingsDatabaseInteractor.updateDatabase(childUpdates);
    }

    public String getArrivedFriends() {
        String usersArrived = "";

        for(User user : users) {

            usersArrived += user.username + " arrived\n";
        }
        return usersArrived;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    @Override
    public void onUserListReady(List<User> users) {
        this.users = users;
    }
}
