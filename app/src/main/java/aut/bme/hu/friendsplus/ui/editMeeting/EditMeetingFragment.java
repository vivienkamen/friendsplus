package aut.bme.hu.friendsplus.ui.editMeeting;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.MyPlace;
import aut.bme.hu.friendsplus.ui.addMeeting.NewMeetingFragment;
import aut.bme.hu.friendsplus.ui.helpers.PickerDialogs;
import aut.bme.hu.friendsplus.ui.listeners.EditMeetingListener;
import aut.bme.hu.friendsplus.ui.listeners.PickerDialogListener;

import static android.app.Activity.RESULT_OK;


public class EditMeetingFragment extends AppCompatDialogFragment implements EditMeetingScreen, PickerDialogListener {

    public static final String TAG = "EditMeetingDialog";
    private static final int PLACE_PICKER_REQUEST = 1;

    private EditText nameEditText;
    private TextView timePickerTextView;
    private TextView datePickerTextView;
    private TextView placePickerTextView;

    private EditMeetingPresenter presenter;
    private EditMeetingListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity instanceof EditMeetingListener) {
            listener = (EditMeetingListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewMeetingListener interface!");
        }

        presenter.attachScreen(this);
    }

    public void setMeeting(Meeting meeting) {

        presenter = new EditMeetingPresenter(meeting);
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
                .setTitle("Edit meeting")
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (isValid()) {
                            listener.onMeetingEdited(presenter.getEditedMeeting());
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
                PickerDialogs.showTimePickerDialog(getActivity(),EditMeetingFragment.this, presenter.getMeetingDate());
            }
        });
        datePickerTextView = (TextView) contentView.findViewById(R.id.DatePickerTextView);
        datePickerTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PickerDialogs.showDatePickerDialog(getActivity(), EditMeetingFragment.this);
            }
        });
        placePickerTextView = (TextView) contentView.findViewById(R.id.PlacePickerTextView);
        placePickerTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = PickerDialogs.viewPlacePickerDialog(getActivity());
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });

        setUI();

        return contentView;
    }

    private boolean isValid() {
        if(TextUtils.isEmpty(nameEditText.getText()))
            return false;
        return true;
    }

    private void setUI() {
        nameEditText.setText(presenter.getMeeting().name);

        Date date = new Date(presenter.getMeeting().meetingDate);
        SimpleDateFormat spf1 = new SimpleDateFormat("yyyy. MM. dd.");
        datePickerTextView.setText(spf1.format(date));

        SimpleDateFormat spf2 = new SimpleDateFormat("HH:mm");
        timePickerTextView.setText(spf2.format(date));

        placePickerTextView.setText(presenter.getMeeting().place.name);
    }

    @Override
    public void onTimeSet(int hour, int minute) {
        presenter.setTime(hour,minute);
    }

    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {

        presenter.setDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onInvalidDate() {
        timePickerTextView.setError(null);
        datePickerTextView.setError("Pick a valid date!");
    }

    @Override
    public void onInvalidTime() {
        timePickerTextView.setError("Pick a valid time!");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(getActivity(), data);
                MyPlace myPlace = new MyPlace(place.getId(), place.getAddress(), place.getName(),
                        place.getLatLng().latitude, place.getLatLng().longitude);
                presenter.setPlace(myPlace);

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
    public String getName() {
        return nameEditText.getText().toString();
    }

    @Override
    public void showValidDate(String date) {
        datePickerTextView.setError(null);
        timePickerTextView.setError(null);
        datePickerTextView.setText(date);
    }


    @Override
    public void showValidTime(String time) {
        timePickerTextView.setError(null);
        timePickerTextView.setText(time);
    }
}
