package aut.bme.hu.friendsplus.ui.meetings.meetingDetails;

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
    private boolean myTrackingStarted;

    public MeetingDetailPresenter(Meeting meeting) {
        authInteractor = new AuthInteractor();
        meetingsDatabaseInteractor = new MeetingsDatabaseInteractor();
        userDatabaseInteractor = new UserDatabaseInteractor();

        meetingsDatabaseInteractor.setTrackingListener(this);
        userDatabaseInteractor.setUsersListener(this);
        userDatabaseInteractor.setUserListListener(this);

        this.meeting = meeting;
        myUID = authInteractor.getCurrentUser().getUid();
        myTrackingStarted = false;
        users = new ArrayList<>();

        if(meeting.position < 0) throw new RuntimeException("Érvénytelen pozíció!");

        meetingsDatabaseInteractor.checkMyTrackingStarted(myUID);
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

    public boolean canEdit() {

        return myUID.equals(meeting.uid)&& !meeting.finished &&
                System.currentTimeMillis() < meeting.meetingDate && !meeting.tracked;
    }

    public void signOut() {
        authInteractor.signOut();
    }

    @Override
    public void onTrackingFound(boolean trackingStarted) {
        this.myTrackingStarted = trackingStarted;
        screen.refreshUI(meeting);

        getUserName();
    }

    public void getUserName() {
        userDatabaseInteractor.getUserByUid(myUID);
    }

    @Override
    public void onUserFound(User user) {
        meetingCreator = user.username;
        screen.setAddedByTextView(" " + meetingCreator);
    }

    @Override
    public void onUserNotFound() {

    }

    public void setStartButton() {
        if(meeting.finished) {
            screen.setStartButtonFinished();
        } else {
            if(meeting.meetingDate < System.currentTimeMillis()) {
                screen.setStartButtonExpired();
                return;
            }
            if(!meeting.tracked) {
                if(!myTrackingStarted) {
                    screen.setStartButtonNoTracking();

                } else {
                    screen.setStartButtonOtherTrackingInProgress();
                }

            } else {
                boolean buttonEnabled = meeting.containsFriend(myUID);
                if(!myTrackingStarted) {
                    buttonEnabled = true;
                }
                screen.setStartButtonThisTrackingInProgress(buttonEnabled);

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

        for(String uid : meeting.getArrivedFriends()) {

            usersArrived += getUser(uid).username + " arrived\n";
        }
        return usersArrived;
    }

    private User getUser(String uid) {
        User user = new User();

        for(User u : users) {
            if(u.uid.equals(uid)) {
                user = u;
            }
        }
        return user;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setEditedMeeting(Meeting meeting) {
        this.meeting = meeting;
        screen.refreshUI(meeting);

        updateMeetingsDatabase();

    }

    @Override
    public void onUserListReady(List<User> users) {
        this.users = users;
    }
}
