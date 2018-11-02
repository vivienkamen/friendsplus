package aut.bme.hu.friendsplus.ui.login.email;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import aut.bme.hu.friendsplus.BuildConfig;
import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.helpers.PermissionChecker;
import aut.bme.hu.friendsplus.ui.login.LoginScreen;
import aut.bme.hu.friendsplus.ui.main.MeetingsActivity;
import aut.bme.hu.friendsplus.ui.signup.SignUpActivity;

public class EmailLoginActivity extends BaseActivity implements LoginScreen, View.OnClickListener {

    private static final String TAG = "EmailLoginActivity";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    EditText editTextEmail;
    EditText editTextPassword;
    ProgressBar progressBar;

    EmailLoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        presenter = new EmailLoginPresenter();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.forgottenPasswordTextView).setOnClickListener(this);

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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.textViewSignup:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.buttonLogin:
                if (validateForm()) {
                    progressBar.setVisibility(View.VISIBLE);
                    presenter.login(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                }
                break;
            case R.id.forgottenPasswordTextView:
                //ResetPasswordDialogFragment resetPasswordDialogFragment = new ResetPasswordDialogFragment();
                //resetPasswordDialogFragment.show(getSupportFragmentManager(), ResetPasswordDialogFragment.TAG);
                break;
        }
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
        if(!PermissionChecker.checkPermissions(this)) {
            PermissionChecker.requestPermissions(this);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid e-mail address");
            editTextEmail.requestFocus();
            valid = false;
        }

        if(TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            valid = false;
        }

        return valid;
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
