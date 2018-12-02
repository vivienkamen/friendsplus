package aut.bme.hu.friendsplus.ui.messages;

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

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.listeners.ItemChangeListener;
import aut.bme.hu.friendsplus.ui.listeners.ItemClickListener;

public class MessagesOverviewAdapter extends RecyclerView.Adapter<MessagesOverviewAdapter.MessageRowViewHolder>
        implements ItemChangeListener {

    public static final String TAG = "MessagesOverviewAdapter";

    private MessagesOverviewPresenter presenter;
    private ItemClickListener listener;

    public MessagesOverviewAdapter(MessagesOverviewPresenter presenter, ItemClickListener listener) {
        this.presenter = presenter;
        this.listener = listener;
        presenter.setItemChangeListener(this);
    }

    @NonNull
    @Override
    public MessageRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageRowViewHolder messageRowViewHolder = new MessageRowViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_friend, parent, false), listener);
        presenter.attachScreen(messageRowViewHolder);
        return  messageRowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRowViewHolder holder, int position) {
        presenter.onBindMessageRowViewAtPosition(position);
    }

    @Override
    public int getItemCount() {
        return presenter.getMeetingRowsCount();
    }

    @Override
    public void onItemChanged(int position) {
        notifyItemChanged(position);
    }

    @Override
    public void onItemInserted(int position) {
        notifyItemInserted(position);
    }

    @Override
    public void onItemRemoved(int position) {
        notifyItemRemoved(position);
    }

    public void refreshItems() {
        notifyDataSetChanged();
    }

    public static class MessageRowViewHolder extends RecyclerView.ViewHolder implements MessagesOverviewScreen, View.OnClickListener {

        TextView nameTextView;
        TextView lastMessageTextView;
        TextView unreadMessagesTextView;
        ImageView imageView;
        Context context;
        public RelativeLayout viewBackground, viewForeground;
        private ItemClickListener listener;
        User friend;

        public MessageRowViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);

            this.listener = listener;
            itemView.setOnClickListener(this);

            context = itemView.getContext();
            nameTextView = (TextView) itemView.findViewById(R.id.textViewName);
            lastMessageTextView = (TextView) itemView.findViewById(R.id.textViewLastMessage);
            unreadMessagesTextView = (TextView) itemView.findViewById(R.id.textViewNewMessage);
            imageView = (ImageView) itemView.findViewById(R.id.account_image);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(friend);
        }

        @Override
        public void setFriend(User friend) {
            this.friend = friend;
            nameTextView.setText(friend.username);
            setImageView();
        }

        public void setImageView() {
            if(friend.imageUri != null) {
                Glide.with(context).load(friend.imageUri).into(imageView);
            }
        }

        @Override
        public void setUnreadMessageTextView(int unreadMessageCount) {
            unreadMessagesTextView.setText("● " + unreadMessageCount);
        }

        @Override
        public void setLastMessage(String message) {
            lastMessageTextView.setText(message);

        }

    }
}
