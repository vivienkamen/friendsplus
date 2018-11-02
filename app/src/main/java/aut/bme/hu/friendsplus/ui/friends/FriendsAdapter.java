package aut.bme.hu.friendsplus.ui.friends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.main.MeetingsAdapter;
import aut.bme.hu.friendsplus.ui.main.MeetingsPresenter;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendRowViewHolder> implements ItemChangeListener {

    public static final String TAG = "FriendsAdapter";

    private FriendsPresenter presenter;

    public FriendsAdapter(FriendsPresenter presenter) {
        this.presenter = presenter;
        presenter.setListener(this);
    }

    @NonNull
    @Override
    public FriendsAdapter.FriendRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FriendsAdapter.FriendRowViewHolder friendRowViewHolder = new FriendsAdapter.FriendRowViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false));
        presenter.attachScreen(friendRowViewHolder);
        return  friendRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendRowViewHolder holder, int position) {
        presenter.onBindFriendRowViewAtPosition(position);
    }

    @Override
    public int getItemCount() {
        return presenter.getFriendRowsCount();
    }

    @Override
    public void onItemChanged(int position) {

        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public static class FriendRowViewHolder extends RecyclerView.ViewHolder implements FriendRowScreen {

        TextView nameTextView;
        TextView emailTextView;
        ImageView imageView;
        Context context;
        public RelativeLayout viewBackground, viewForeground;
        public User user;

        public FriendRowViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            nameTextView = (TextView) itemView.findViewById(R.id.textViewName);
            emailTextView = (TextView) itemView.findViewById(R.id.textViewEmail);
            imageView = (ImageView) itemView.findViewById(R.id.account_image);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }

        @Override
        public void setFriend(User user) {
            this.user = user;

            nameTextView.setText(user.username);
            emailTextView.setText(user.email);

            setImage();
        }

        private void setImage() {

            if(user.imageUri != null) {
                Glide.with(context).load(user.imageUri).into(imageView);
            }
        }
    }
}
