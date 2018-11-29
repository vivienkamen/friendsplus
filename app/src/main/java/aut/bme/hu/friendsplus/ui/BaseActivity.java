package aut.bme.hu.friendsplus.ui;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    private View snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        snackbar = findViewById(android.R.id.content);

    }

    public void showSnackbar(String message, String actionString,
                                View.OnClickListener listener) {
        Snackbar.make(
                snackbar,
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionString, listener).show();

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void signOut() {}
}
