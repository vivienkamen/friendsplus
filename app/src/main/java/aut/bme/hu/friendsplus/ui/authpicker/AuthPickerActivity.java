package aut.bme.hu.friendsplus.ui.authpicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;


import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.login.email.EmailLoginActivity;
import aut.bme.hu.friendsplus.ui.login.facebook.FacebookLoginActivity;
import aut.bme.hu.friendsplus.ui.login.google.GoogleLoginActivity;
import aut.bme.hu.friendsplus.ui.meetings.MeetingsActivity;


public class AuthPickerActivity extends BaseActivity implements AuthPickerScreen, View.OnClickListener {

    private static final String TAG = "AuthPickerActivity";
    AuthPickerPresenter presenter;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_picker);

        presenter = new AuthPickerPresenter();
        presenter.init();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.email_button).setOnClickListener(this);
        findViewById(R.id.google_button).setOnClickListener(this);
        findViewById(R.id.facebook_button).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachScreen(this);
        presenter.checkIfLoggedIn();
    }

    @Override
    protected void onStop() {
        presenter.detachScreen();
        super.onStop();
    }

    @Override
    public void navigateToEmailLogin() {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(this, MeetingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }

    @Override
    public void navigateToFacebookLogin() {
        Intent intent = new Intent(this, FacebookLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToGoogleLogin() {
        Intent intent = new Intent(this, GoogleLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_button:
                presenter.emailLogin();
                break;
            case R.id.google_button:
                presenter.googleLogin();
                break;
            case R.id.facebook_button:
                presenter.facebookLogin();
                break;
        }
    }


}
