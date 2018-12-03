package aut.bme.hu.friendsplus.ui.meetings;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.MeetingsDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.util.FriendsProvider;
import aut.bme.hu.friendsplus.ui.listeners.FriendsReadyListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.MeetingsListener;
import aut.bme.hu.friendsplus.ui.listeners.UserListListener;

public class MeetingsPresenter extends Presenter<MeetingRowScreen> implements UserListListener, MeetingsListener, FriendsReadyListener {

    private MeetingsDatabaseInteractor meetingsDatabaseInteractor;
    private UserDatabaseInteractor userDatabaseInteractor;
    private AuthInteractor authInteractor;

    private ItemChangeListener itemChangeListener;

    private List<Meeting> meetings;
    private List<String> keys;
    private List<User> users;
    private FriendsProvider friendsProvider;

    public MeetingsPresenter() {

        friendsProvider = new FriendsProvider(this);

        meetingsDatabaseInteractor = new MeetingsDatabaseInteractor();
        userDatabaseInteractor = new UserDatabaseInteractor();
        authInteractor = new AuthInteractor();

        meetingsDatabaseInteractor.setMeetingsListener(this);
        userDatabaseInteractor.setUserListListener(this);

        meetings = new ArrayList<>();
        keys = new ArrayList<>();
        users = new ArrayList<>();

    }

    @Override
    public void onFriendsReady() {
        userDatabaseInteractor.getUsers(friendsProvider.getFriendsList());
    }

    @Override
    public void onUserListReady(List<User> users) {
        this.users = users;
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
        screen.setImage(getUserFromUID(meeting.uid).imageUri);
    }

    public int getMeetingRowsCount() {
        return meetings.size();
    }

    public void setItemChangeListener(ItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    private User getUserFromUID(String uid) {
        User user1 = new User();
        for(User user2 : users) {
            if(user2.uid.equals(uid)) {
                user1 = user2;
            }
        }
        return user1;
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

            itemChangeListener.onItemInserted(meetings.size() - 1);
        }
    }

    @Override
    public void onMeetingChanged(Meeting meeting, String key) {
        if(friendsProvider.isFriend(meeting.uid)) {
            int meetingIndex = keys.indexOf(key);
            if (meetingIndex > -1) {

                meetings.set(meetingIndex, meeting);
                itemChangeListener.onItemChanged(meetingIndex);
            }
        }

    }

    @Override
    public void onMeetingRemoved(String key) {

        int meetingIndex = keys.indexOf(key);
        if (meetingIndex > -1) {

            keys.remove(meetingIndex);
            meetings.remove(meetingIndex);

            itemChangeListener.onItemRemoved(meetingIndex);
        }
    }

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
