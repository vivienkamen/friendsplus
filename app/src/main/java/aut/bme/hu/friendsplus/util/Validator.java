package aut.bme.hu.friendsplus.util;

import java.util.Calendar;

public class Validator {

    public static boolean validateDate(int year, int monthOfYear, int dayOfMonth) {

        Calendar today = Calendar.getInstance();

        Calendar meetingDate = Calendar.getInstance();
        meetingDate.set(year, monthOfYear, dayOfMonth);

        return meetingDate.after(today);
    }

    public static boolean validateTime(int hour, int minute, Calendar meetingDate) {
        Calendar today = Calendar.getInstance();

        meetingDate.set(Calendar.HOUR_OF_DAY, hour);
        meetingDate.set(Calendar.MINUTE, minute);

        return meetingDate.after(today);

    }
}
