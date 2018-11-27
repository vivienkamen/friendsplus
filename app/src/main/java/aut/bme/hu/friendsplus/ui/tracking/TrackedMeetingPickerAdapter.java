package aut.bme.hu.friendsplus.ui.tracking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.R;

public class TrackedMeetingPickerAdapter extends ArrayAdapter<Meeting> {

    public TrackedMeetingPickerAdapter(@NonNull Context context, ArrayList<Meeting> meetings) {
        super(context, -1, meetings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Meeting meeting = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tracked_meeting, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        nameTextView.setText(meeting.name);

        return convertView;
    }

}
