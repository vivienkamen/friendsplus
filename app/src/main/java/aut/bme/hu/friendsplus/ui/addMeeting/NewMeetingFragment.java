package aut.bme.hu.friendsplus.ui.addMeeting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.MyPlace;
import aut.bme.hu.friendsplus.ui.helpers.PickerDialogs;
import aut.bme.hu.friendsplus.ui.listeners.NewMeetingListener;
import aut.bme.hu.friendsplus.ui.listeners.PickerDialogListener;

import static android.app.Activity.RESULT_OK;

public class NewMeetingFragment extends AppCompatDialogFragment implements NewMeetingScreen, PickerDialogListener {

    public static final String TAG = "NewMeetingFragment";
    private static final int PLACE_PICKER_REQUEST = 1;

    private NewMeetingListener listener;

    private EditText nameEditText;
    private TextView timePickerTextView;
    private TextView datePickerTextView;
    private TextView placePickerTextView;

    private Meeting meeting;
    private Calendar meetingDate;
    private NewMeetingPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meeting = new Meeting();
        meetingDate = Calendar.getInstance();
        meetingDate.clear();

        presenter = new NewMeetingPresenter();

        FragmentActivity activity = getActivity();
        if (activity instanceof NewMeetingListener) {
            listener = (NewMeetingListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewMeetingListener interface!");
        }

        presenter.attachScreen(this);
    }

    @Override
    public void onStop() {
        presenter.detachScreen();
        super.onStop();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_meeting)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (isValid()) {
                            listener.onMeetingCreated(getMeeting());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_new_meeting, null);
        nameEditText = (EditText) contentView.findViewById(R.id.MeetingNameEditText);
        timePickerTextView = (TextView) contentView.findViewById(R.id.TimePickerTextView);
        timePickerTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PickerDialogs.showTimePickerDialog(getActivity(),NewMeetingFragment.this);
            }
        });
        datePickerTextView = (TextView) contentView.findViewById(R.id.DatePickerTextView);
        datePickerTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PickerDialogs.showDatePickerDialog(getActivity(), NewMeetingFragment.this);
            }
        });
        placePickerTextView = (TextView) contentView.findViewById(R.id.PlacePickerTextView);
        placePickerTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = PickerDialogs.viewPlacePickerDialog(getActivity());
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });

        return contentView;
    }

    @Override
    public boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    @Override
    public void onTimeSet(int hour, int minute) {

        timePickerTextView.setText(String.valueOf(hour) + " : " + String.valueOf(minute));

        meetingDate.set(Calendar.HOUR_OF_DAY, hour);
        meetingDate.set(Calendar.MINUTE, minute);
    }

    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {

        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear + 1)
                +"-"+String.valueOf(dayOfMonth);

        datePickerTextView.setText(date);

        meetingDate.set(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(getActivity(), data);
                MyPlace myPlace = new MyPlace(place.getId(), place.getAddress(), place.getName(),
                        place.getLatLng().latitude, place.getLatLng().longitude);
                meeting.place = myPlace;

                final CharSequence name = place.getName();
                final String placeId = place.getId();

                placePickerTextView.setText(name);

                Log.d(TAG, "Place selected: " + placeId + " (" + name.toString() + ")");


            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Meeting getMeeting() {

        meeting.name = nameEditText.getText().toString();

        meeting.uid = presenter.getUID();

        meeting.meetingDate = meetingDate.getTimeInMillis();

        meeting.creationTime = System.currentTimeMillis();

        return meeting;
    }
}
