package aut.bme.hu.friendsplus.ui.addMeeting;

import java.util.Calendar;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.MyPlace;
import aut.bme.hu.friendsplus.ui.Presenter;

public class NewMeetingPresenter extends Presenter<NewMeetingScreen> {

    private AuthInteractor authInteractor;
    Meeting meeting;
    Calendar meetingDate;
    boolean dateSet;

    public NewMeetingPresenter() {

        authInteractor = new AuthInteractor(null, null);
        meeting = new Meeting();
        meetingDate = Calendar.getInstance();
        meetingDate.clear();
        dateSet = false;
    }

    @Override
    public void attachScreen(NewMeetingScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public Calendar getMeetingDate() {
        return meetingDate;
    }

    public void setTime(int hour, int minute) {

        meetingDate.set(Calendar.HOUR_OF_DAY, hour);
        meetingDate.set(Calendar.MINUTE, minute);
        String time = String.valueOf(hour) + " : " + String.valueOf(minute);
        screen.showValidTime(time);

    }

    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        meetingDate.set(year, monthOfYear, dayOfMonth);
        dateSet = true;
        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear + 1)
                +"-"+String.valueOf(dayOfMonth);
        screen.showValidDate(date);

    }

    public void setPlace(MyPlace place) {
        meeting.place = place;
    }

    public Meeting getNewMeeting() {
        meeting.name = screen.getName();
        meeting.uid = authInteractor.getCurrentUser().getUid();
        meeting.meetingDate = meetingDate.getTimeInMillis();
        meeting.creationTime = System.currentTimeMillis();

        return meeting;
    }

    public boolean isDateSet() {
        return dateSet;
    }
}
