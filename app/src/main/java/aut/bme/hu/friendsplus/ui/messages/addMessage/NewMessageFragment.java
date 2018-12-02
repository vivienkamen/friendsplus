package aut.bme.hu.friendsplus.ui.messages.addMessage;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.listeners.MessagingStartedListener;

public class NewMessageFragment extends AppCompatDialogFragment implements NewMessageScreen, AdapterView.OnItemClickListener {

    public static final String TAG = "NewMessageFragment";

    NewMessagePresenter presenter;
    MessagingStartedListener listener;
    boolean nothingSelected;

    AutoCompleteTextView sendToTextView;
    EditText messageEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NewMessagePresenter();
        nothingSelected = true;

        FragmentActivity activity = getActivity();
        if (activity instanceof MessagingStartedListener) {
            listener = (MessagingStartedListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the MessagingStarted interface!");
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
                .setTitle("New message")
                .setView(getContentView())
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (isValid()) {
                            listener.onMessagingStarted(presenter.getMessage(), presenter.getFriendUID());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_new_message, null);

        sendToTextView = (AutoCompleteTextView) contentView.findViewById(R.id.sendToEditText);
        messageEditText = (EditText) contentView.findViewById(R.id.messageEditText);

        return contentView;
    }



    public boolean isValid() {
        if(TextUtils.isEmpty(messageEditText.getText()))
            return false;
        if(nothingSelected)
            return false;

        return true;

    }

    @Override
    public String getMessageText() {
        return messageEditText.getText().toString();
    }

    @Override
    public void setSendToTextView(ArrayList<String> friends) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friends);
        sendToTextView.setAdapter(adapter);

        sendToTextView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        nothingSelected = false;
        String selectedFriend = (String) parent.getItemAtPosition(position);
        presenter.setSelectedFriend(selectedFriend);
    }
}
