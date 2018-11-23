package aut.bme.hu.friendsplus.ui.main;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.FriendsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.MeetingsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.helpers.FriendsProvider;
import aut.bme.hu.friendsplus.ui.listeners.FriendsReadyListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.MeetingsListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class MeetingsPresenter extends Presenter<MeetingRowScreen> implements UsersListener, MeetingsListener, FriendsReadyListener {

    private MeetingsDatabaseInteractor meetingsDatabaseInteractor;
    private UserDatabaseInteractor userDatabaseInteractor;
    private AuthInteractor authInteractor;

    private ItemChangeListener listener;

    private List<Meeting> meetings;
    private List<String> keys;
    private FriendsProvider friendsProvider;

    public MeetingsPresenter() {

        friendsProvider = new FriendsProvider(this);

        meetingsDatabaseInteractor = new MeetingsDatabaseInteractor(this, null);
        userDatabaseInteractor = new UserDatabaseInteractor(this, null);
        authInteractor = new AuthInteractor(null, null);

        meetings = new ArrayList<>();
        keys = new ArrayList<>();

    }

    @Override
    public void onFriendsReady() {
        meetingsDatabaseInteractor.addMeetingsListener();
    }

    @Override
    public void attachScreen(MeetingRowScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void onBindMeetingRowViewAtPosition(int position) {
        Meeting meeting = meetings.get(position);
        screen.setMeeting(meeting);
        userDatabaseInteractor.getUserByUid(meeting.uid);
    }

    public int getMeetingRowsCount() {
        return meetings.size();
    }

    public void setListener(ItemChangeListener listener) {
        this.listener = listener;
    }

    public void signOut() {
        authInteractor.signOut();
    }

    @Override
    public void onMeetingAdded(Meeting meeting, String key) {

        if(friendsProvider.isFriend(meeting.uid)) {
            meeting.position = meetings.size();
            meetings.add(meeting);
            keys.add(key);

            listener.onItemChanged(meetings.size() - 1);
        }
    }

    @Override
    public void onMeetingChanged(Meeting meeting, String key) {
        if(friendsProvider.isFriend(meeting.uid)) {
            int meetingIndex = keys.indexOf(key);
            if (meetingIndex > -1) {

                meetings.set(meetingIndex, meeting);
                listener.onItemChanged(meetingIndex);
            }
        }

    }

    @Override
    public void onMeetingRemoved(String key) {

        int meetingIndex = keys.indexOf(key);
        if (meetingIndex > -1) {

            keys.remove(meetingIndex);
            meetings.remove(meetingIndex);

            listener.onItemChanged(meetingIndex);
        }
    }

    @Override
    public void onUserFound(User user) {
        screen.setImage(user);
    }

    @Override
    public void onUserNotFound() {}


    public Meeting getMeeting(int position) {
        return meetings.get(position);
    }

    public void addMeeting(Meeting meeting) {
        meetingsDatabaseInteractor.addMeeting(meeting);
    }

    public String removeMeeting(int position) {
        String key = meetings.get(position).key;
        meetingsDatabaseInteractor.removeMeeting(key);

        return key;
    }

    public void restoreMeeting(Meeting meeting, String key) {
        meetingsDatabaseInteractor.restoreMeeting(meeting, key);
    }

    public void cleanupListener() {
        meetingsDatabaseInteractor.cleanupListener();
    }

}
