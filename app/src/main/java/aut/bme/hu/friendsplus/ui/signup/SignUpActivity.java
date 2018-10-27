package aut.bme.hu.friendsplus.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.login.email.EmailLoginActivity;


public class SignUpActivity extends BaseActivity implements SignUpScreen, View.OnClickListener{

    private EditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName;
    private ProgressBar progressBar;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        presenter = new SignUpPresenter();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                if(validateForm()) {
                    progressBar.setVisibility(View.VISIBLE);
                    presenter.signUp(getEmail(), editTextPassword.getText().toString());
                }
                break;

        }

    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, EmailLoginActivity.class);
        //TODO
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        finish();
    }

    @Override
    public void showSignUpSuccess() {
        progressBar.setVisibility(View.GONE);
        showToast("Account created successfully.");

    }

    @Override
    public void showSignUpFailure(String message) {
        progressBar.setVisibility(View.GONE);
        showToast(message);

    }

    @Override
    public String getUserName() {
        return editTextFirstName.getText().toString() + " " + editTextLastName.getText().toString();
    }

    @Override
    public String getEmail() {
        return editTextEmail.getText().toString();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = getEmail();
        String password = editTextPassword.getText().toString();
        String firstName = editTextFirstName.getText().toString();


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

        if(password.length() < 6) {
            editTextPassword.setError("Minimum password length is 6");
            editTextPassword.requestFocus();
            valid = false;
        }

        if(TextUtils.isEmpty(firstName)) {
            editTextFirstName.setError("Required");
            editTextFirstName.requestFocus();
            valid = false;
        }


        return valid;
    }
}
