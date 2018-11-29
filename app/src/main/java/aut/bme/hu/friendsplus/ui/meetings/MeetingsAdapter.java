package aut.bme.hu.friendsplus.ui.meetings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemClickListener;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MeetingRowViewHolder>
        implements ItemChangeListener {

    public static final String TAG = "MeetingsAdapter";

    private MeetingsPresenter presenter;
    private ItemClickListener listener;

    public MeetingsAdapter(MeetingsPresenter presenter, ItemClickListener listener) {

        this.presenter = presenter;
        this.listener = listener;
        presenter.setListener(this);
    }


    @NonNull
    @Override
    public MeetingRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MeetingRowViewHolder meetingRowViewHolder = new MeetingRowViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false), listener);
        presenter.attachScreen(meetingRowViewHolder);
        return  meetingRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingRowViewHolder holder, int position) {

        presenter.onBindMeetingRowViewAtPosition(position);
    }

    @Override
    public int getItemCount() {
        return presenter.getMeetingRowsCount();
    }

    @Override
    public void onItemChanged(int position) {
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void refreshItems() {
        notifyDataSetChanged();
    }

    public static class MeetingRowViewHolder extends RecyclerView.ViewHolder implements MeetingRowScreen, View.OnClickListener {

        TextView nameTextView;
        TextView placeTextView;
        TextView dateTextView;
        TextView trackingTextView;
        TextView expiredTextView;
        TextView finishedTextView;
        ImageView imageView;
        Context context;
        public RelativeLayout viewBackground, viewForeground;
        private ItemClickListener listener;
        private Meeting meeting;


        public MeetingRowViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);

            this.listener = listener;
            itemView.setOnClickListener(this);

            context = itemView.getContext();
            nameTextView = (TextView) itemView.findViewById(R.id.textViewName);
            placeTextView = (TextView) itemView.findViewById(R.id.textViewPlace);
            dateTextView = (TextView) itemView.findViewById(R.id.textViewDate);
            trackingTextView = (TextView) itemView.findViewById(R.id.trackingTextView);
            expiredTextView = (TextView) itemView.findViewById(R.id.expiredTextView);
            finishedTextView = (TextView) itemView.findViewById(R.id.finishedTextView);
            imageView = (ImageView) itemView.findViewById(R.id.account_image);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        @Override
        public void setMeeting(Meeting meeting) {
            clearTextViews();
            this.meeting = meeting;
            nameTextView.setText(meeting.name);
            placeTextView.setText(meeting.place.name);
            if(meeting.tracked) {
                trackingTextView.setVisibility(View.VISIBLE);
            }
            if(meeting.finished) {
                finishedTextView.setVisibility(View.VISIBLE);
            }
            else if(meeting.meetingDate < System.currentTimeMillis()) {
                expiredTextView.setVisibility(View.VISIBLE);
            }
            Date date = new Date(meeting.meetingDate);
            SimpleDateFormat spf = new SimpleDateFormat("yyyy. MM. dd. HH:mm");
            dateTextView.setText(spf.format(date));

        }

        private void clearTextViews() {
            trackingTextView.setVisibility(View.GONE);
            finishedTextView.setVisibility(View.GONE);
            expiredTextView.setVisibility(View.GONE);

        }

        @Override
        public void setImage(User user) {

            if(user.imageUri != null) {
                Glide.with(context).load(user.imageUri).into(imageView);
            }
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(meeting);
        }
    }
}
