package aut.bme.hu.friendsplus.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;

public class MeetingsActivity extends BaseActivity implements View.OnClickListener{

    AuthInteractor authInteractor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);

        findViewById(R.id.btn_sign_out).setOnClickListener(this);
        authInteractor = new AuthInteractor(null);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_out:
                authInteractor.signOut();

                Intent intent = new Intent(this, AuthPickerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
