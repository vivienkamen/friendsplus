package aut.bme.hu.friendsplus.ui.meetings.editMeeting;

import java.util.Calendar;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.MyPlace;
import aut.bme.hu.friendsplus.ui.Presenter;

public class EditMeetingPresenter extends Presenter<EditMeetingScreen> {

    Meeting meeting;
    Calendar meetingDate;

    public EditMeetingPresenter(Meeting meeting) {
        this.meeting = meeting;
        meetingDate = Calendar.getInstance();
        meetingDate.clear();
        meetingDate.setTimeInMillis(meeting.meetingDate);
    }

    @Override
    public void attachScreen(EditMeetingScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
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
        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear + 1)
                +"-"+String.valueOf(dayOfMonth);
        screen.showValidDate(date);
    }

    public void setPlace(MyPlace place) {
        meeting.place = place;
    }

    public Meeting getEditedMeeting() {
        meeting.name = screen.getName();

        meeting.meetingDate = meetingDate.getTimeInMillis();

        meeting.creationTime = System.currentTimeMillis();

        return meeting;
    }


}
