package aut.bme.hu.friendsplus.ui.login.google;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import aut.bme.hu.friendsplus.BuildConfig;
import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;
import aut.bme.hu.friendsplus.ui.helpers.PermissionChecker;
import aut.bme.hu.friendsplus.ui.meetings.MeetingsActivity;

public class GoogleLoginActivity extends BaseActivity implements GoogleLoginScreen {

    private static final int RC_SIGN_IN = 500;
    private static final String TAG = "GoogleLoginActivity";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    GoogleSignInClient googleSignInClient;
    GoogleLoginPresenter presenter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("735358910342-5qr75nss9r9uhtpq7fst235unkl9vim8.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        presenter = new GoogleLoginPresenter();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        googleSignIn();
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

    @Override
    public void checkPermission() {
        if(!PermissionChecker.checkLocationPermission(this)) {
            PermissionChecker.requestLocationPermission(this);
        }
    }

    @Override
    public void navigateBack() {
        Intent intent = new Intent(this, AuthPickerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void googleSignIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.googleLogin(account);
            } catch (ApiException e) {

                Log.w(TAG, "Google sign in failed", e);
                navigateBack();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                showSnackbar(getString(R.string.permission_denied_explanation),
                        getString(R.string.settings), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }
}
