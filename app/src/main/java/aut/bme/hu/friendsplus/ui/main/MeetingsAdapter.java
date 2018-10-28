package aut.bme.hu.friendsplus.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MeetingsViewHolder> {

    public static final String TAG = "MeetingsAdapter";


    public List<Meeting> meetings;
    public List<String> keys;
    private Friends friends;
    private List<User> users;
    private Context context;
    private ItemClickListener listener;
    private DatabaseReference mDatabase;
    private ChildEventListener mChildEventListenerMeetings;

    public interface ItemClickListener {
        void onItemClick(Meeting meeting, int position);

    }


    public MeetingsAdapter(ItemClickListener listener, Context context) {


        meetings = new ArrayList<>();
        keys = new ArrayList<>();
        users = new ArrayList<>();
        friends = new Friends();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.listener = listener;
        this.context = context;

        addUsers();

    }

    private void addUsers() {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot friendsDataSnapshot : dataSnapshot.getChildren()) {
                    User user = friendsDataSnapshot.getValue(User.class);
                    users.add(user);
                }
                addMeetingChildEventListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    private void addMeetingChildEventListener() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                Meeting meeting = dataSnapshot.getValue(Meeting.class);

                if(friends.isFriend(meeting.uid)) {
                    meeting.position = meetings.size();
                    meetings.add(meeting);
                    keys.add(dataSnapshot.getKey());
                    notifyItemInserted(meetings.size() - 1);
                    notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                Meeting newMeeting = dataSnapshot.getValue(Meeting.class);

                if(friends.isFriend(newMeeting.uid)) {

                    String meetingKey = dataSnapshot.getKey();

                    int meetingIndex = keys.indexOf(meetingKey);
                    if (meetingIndex > -1) {

                        meetings.set(meetingIndex, newMeeting);

                        notifyItemChanged(meetingIndex);
                        notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + meetingKey);
                    }
                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


                String meetingKey = dataSnapshot.getKey();

                int meetingIndex = keys.indexOf(meetingKey);
                if (meetingIndex > -1) {

                    keys.remove(meetingIndex);
                    meetings.remove(meetingIndex);

                    notifyItemRemoved(meetingIndex);
                    notifyDataSetChanged();
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + meetingKey);
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "meetings:onCancelled", databaseError.toException());

            }
        };
        mDatabase.child("meetings").addChildEventListener(childEventListener);

        mChildEventListenerMeetings = childEventListener;
    }



    public void cleanupListener() {
        if (mChildEventListenerMeetings != null) {
            mDatabase.child("meetings").removeEventListener(mChildEventListenerMeetings);


        }
    }

    @Override
    public MeetingsAdapter.MeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        MeetingsViewHolder viewHolder = new MeetingsViewHolder(itemView, listener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetingsViewHolder holder, int position) {


        final Meeting item = meetings.get(position);
        holder.nameTextView.setText(item.name);
        holder.placeTextView.setText(item.place.name);
        if(!item.tracked) {
            holder.trackingTextView.setVisibility(View.GONE);
        }
        Date date = new Date(item.meetingDate);
        SimpleDateFormat spf = new SimpleDateFormat("yyyy. MM. dd. HH:mm");
        holder.dateTextView.setText(spf.format(date));
        holder.setMeeting(item);
        holder.setPos(position);

        setImage(item.uid,holder.imageView, position);
    }

    private void setImage(String uid, ImageView imageView, int position) {

        User user = new User();

        for(User u : users) {
            if(u.uid.equals(uid)) {
                user = u;
            }
        }


        if(user.imageUri != null) {
            Glide.with(context).load(user.imageUri).into(imageView);
        }
    }


    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public void addItem(Meeting meeting) {

        String key = mDatabase.child("meetings").push().getKey();
        meeting.key = key;
        mDatabase.child("meetings").child(key).setValue(meeting);

    }

    public String removeItem(int position) {
        String key = meetings.get(position).key;
        mDatabase.child("meetings").child(key).removeValue();

        return key;
    }


    public Meeting getMeeting(int position) {
        return meetings.get(position);
    }

    public void restoreItem(Meeting meeting, String key) {
        mDatabase.child("meetings").child(key).setValue(meeting);


    }

    public static class MeetingsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView nameTextView;
        TextView placeTextView;
        TextView dateTextView;
        TextView trackingTextView;
        ImageView imageView;
        public RelativeLayout viewBackground, viewForeground;
        private ItemClickListener listener;
        private Meeting meeting;
        private int pos;

        public MeetingsViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);

            this.listener = listener;
            itemView.setOnClickListener(this);

            nameTextView = (TextView) itemView.findViewById(R.id.textViewName);
            placeTextView = (TextView) itemView.findViewById(R.id.textViewPlace);
            dateTextView = (TextView) itemView.findViewById(R.id.textViewDate);
            trackingTextView = (TextView) itemView.findViewById(R.id.trackingTextView);
            imageView = (ImageView) itemView.findViewById(R.id.account_image);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(meeting, pos);
        }

        public void setMeeting(Meeting meeting) {this.meeting = meeting;}

        public void setPos(int pos) {this.pos = pos;}
    }
}
