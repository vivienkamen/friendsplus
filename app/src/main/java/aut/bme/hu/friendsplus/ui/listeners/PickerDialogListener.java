package aut.bme.hu.friendsplus.ui.listeners;

public interface PickerDialogListener {
    void onTimeSet(int hour, int minute);
    void onDateSet(int year, int monthOfYear, int dayOfMonth);
    void onInvalidDate();
    void onInvalidTime();
}
