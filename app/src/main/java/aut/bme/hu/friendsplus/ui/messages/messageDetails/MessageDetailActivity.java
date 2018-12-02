package aut.bme.hu.friendsplus.ui.messages.messageDetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.Message;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.BaseActivity;

public class MessageDetailActivity extends BaseActivity implements MessageDetailScreen {

    MessageDetailPresenter presenter;

    FloatingActionButton sendButton;
    EditText messageInput;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        User user = (User) getIntent().getParcelableExtra("User");
        presenter = new MessageDetailPresenter(user);

        setTitle(user.username);

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachScreen(this);
        presenter.setAdapter(this);

    }

    @Override
    protected void onStop() {
        presenter.detachScreen();
        super.onStop();
    }

    private void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.arrow_back_white_24dp);

        sendButton = (FloatingActionButton) findViewById(R.id.sendButton);
        messageInput = (EditText) findViewById(R.id.inputMessage);
        listView = (ListView) findViewById(R.id.messagesList);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(messageInput.getText().toString());
                messageInput.setText("");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void setMyMessageLayout(View view, String messageText) {
        RelativeLayout myMessageLayout = (RelativeLayout) view.findViewById(R.id.myMessageLayout);
        RelativeLayout friendMessageLayout = (RelativeLayout) view.findViewById(R.id.friendMessageLayout);

        TextView myMessageTextView = (TextView) view.findViewById(R.id.textViewMyMessage);

        myMessageTextView.setText(messageText);
        myMessageLayout.setVisibility(View.VISIBLE);
        friendMessageLayout.setVisibility(View.GONE);


    }

    @Override
    public void setFriendMessageLayout(View view, String messageText) {

        RelativeLayout friendMessageLayout = (RelativeLayout) view.findViewById(R.id.friendMessageLayout);
        RelativeLayout myMessageLayout = (RelativeLayout) view.findViewById(R.id.myMessageLayout);

        ImageView imageView = (ImageView) view.findViewById(R.id.account_image);
        TextView nameTextView = (TextView) view.findViewById(R.id.textViewName);
        TextView messageTextView = (TextView) view.findViewById(R.id.textViewFriendMessage);

        User friend = presenter.getFriend();
        if(friend.imageUri != null) {
            Glide.with(this).load(friend.imageUri).into(imageView);
        }
        nameTextView.setText(friend.username);
        messageTextView.setText(messageText);
        friendMessageLayout.setVisibility(View.VISIBLE);
        myMessageLayout.setVisibility(View.GONE);
    }


    @Override
    public void setAdapter(FirebaseListAdapter<Message> adapter) {
        listView.setAdapter(adapter);
    }
}
