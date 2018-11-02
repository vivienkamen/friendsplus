package aut.bme.hu.friendsplus.ui.addFriend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.addMeeting.NewMeetingPresenter;
import aut.bme.hu.friendsplus.ui.listeners.NewFriendListener;


public class NewFriendFragment extends AppCompatDialogFragment implements NewFriendScreen{

    public static final String TAG = "NewFriendFragment";

    private EditText emailEditText;
    private Button searchButton;
    private ImageView accountImage;
    private TextView nameTextView;
    private LinearLayout accountLinearLayout;

    User user;

    NewFriendListener newFriendListener;
    NewFriendPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NewFriendPresenter();
        user = new User();

        presenter.attachScreen(this);

        FragmentActivity activity = getActivity();
        if (activity instanceof NewFriendListener) {
            newFriendListener = (NewFriendListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewFriendListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Add new friend")
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(isValid()) {
                            newFriendListener.onFriendCreated(user);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_new_friend, null);

        emailEditText = (EditText) contentView.findViewById(R.id.emailEditText);
        searchButton = (Button) contentView.findViewById(R.id.searchButton);
        accountImage = (ImageView) contentView.findViewById(R.id.accountImage);
        nameTextView = (TextView) contentView.findViewById(R.id.nameTextView);
        accountLinearLayout = (LinearLayout) contentView.findViewById(R.id.accountInformation);

        accountLinearLayout.setVisibility(View.GONE);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.searchUser(emailEditText.getText().toString());
            }
        });


        return contentView;
    }

    private boolean isValid() {
        boolean valid = true;

        if(TextUtils.isEmpty(emailEditText.getText()) || user == null) {
            emailEditText.setError("Enter a valid e-mail address");
            emailEditText.requestFocus();
            valid = false;
        }

        return valid;
    }

    @Override
    public void updateUI() {
        accountLinearLayout.setVisibility(View.VISIBLE);
        setImage();

        nameTextView.setText(user.username);
    }

    @Override
    public void setImage() {

        if(user.imageUri != null) {
            Glide.with(getContext()).load(user.imageUri).into(accountImage);
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setError() {

        emailEditText.setError("E-mail does not exist");
        emailEditText.requestFocus();
    }

}
