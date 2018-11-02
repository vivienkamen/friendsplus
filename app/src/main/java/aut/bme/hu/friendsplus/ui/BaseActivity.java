package aut.bme.hu.friendsplus.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.main.MeetingsActivity;

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
