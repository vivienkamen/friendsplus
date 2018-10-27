package aut.bme.hu.friendsplus.ui.login.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;
import aut.bme.hu.friendsplus.ui.main.MeetingsActivity;

public class FacebookLoginActivity extends BaseActivity implements FacebookLoginScreen{

    private static final String TAG = "FacebookLoginActivity";
    FacebookLoginPresenter presenter;

    ProgressBar progressBar;

    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        presenter = new FacebookLoginPresenter();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        login();

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachScreen(this);
    }

    @Override
    protected void onStop() {
        presenter.detachScreen();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void navigateBack() {
        Intent intent = new Intent(this, AuthPickerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public void navigateToMain() {
        Intent intent = new Intent(this, MeetingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }

    @Override
    public void showLoginFailure(String message) {
        progressBar.setVisibility(View.GONE);
        showToast(message);
    }

    @Override
    public void showSuccessfulLogin() {
        progressBar.setVisibility(View.GONE);
    }

    private void login() {
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                presenter.loginToFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        loginButton.performClick();
    }

}
