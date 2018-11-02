package aut.bme.hu.friendsplus.ui.helpers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Calendar;

import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.listeners.PickerDialogListener;

public class PickerDialogs {

    private static final int PLACE_PICKER_REQUEST = 1;

    public static void showTimePickerDialog(FragmentActivity activity, final PickerDialogListener listener) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {

                listener.onTimeSet(hh, mm);

            }
        }, hour, minute, DateFormat.is24HourFormat(activity));
        timePicker.show();
    }

    public static void showDatePickerDialog(FragmentActivity activity, final PickerDialogListener listener) {
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                listener.onDateSet(year, monthOfYear, dayOfMonth);

            }
        }, yy, mm, dd);
        datePicker.show();
    }

    public static Intent viewPlacePickerDialog(FragmentActivity activity) {

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(activity);

            return intent;



        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            apiAvailability.getErrorDialog(activity, e.getConnectionStatusCode(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(activity, "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }

        return null;
    }
}
