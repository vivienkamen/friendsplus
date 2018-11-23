package aut.bme.hu.friendsplus.ui.meetingDetails;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import aut.bme.hu.friendsplus.BuildConfig;
import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.services.TrackingService;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;
import aut.bme.hu.friendsplus.ui.editMeeting.EditMeetingFragment;
import aut.bme.hu.friendsplus.ui.helpers.NavigationDrawer;
import aut.bme.hu.friendsplus.ui.helpers.PermissionChecker;
import aut.bme.hu.friendsplus.ui.listeners.EditMeetingListener;
import aut.bme.hu.friendsplus.ui.tracking.TrackingActivity;

public class MeetingDetailActivity extends BaseActivity implements MeetingDetailScreen, OnMapReadyCallback,
        EditMeetingListener {

    private static final String TAG = "MeetingDetailActivity";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private MeetingDetailPresenter presenter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawer navigationDrawer;
    private MapFragment mapFragment;
    private GoogleMap map;
    private TextView placeTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private TextView addedByTextView;
    private TextView arrivedTextView;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

        Meeting meeting = (Meeting) getIntent().getParcelableExtra("Meeting");
        presenter = new MeetingDetailPresenter(meeting);

        initUI();
        initDrawerLayout();
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

    private void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        placeTextView = (TextView) findViewById(R.id.placeTextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        arrivedTextView = (TextView) findViewById(R.id.arrivedTextView);
        startButton = (Button) findViewById(R.id.startButton);
        addedByTextView = (TextView) findViewById(R.id.addedByTextView2);

    }

    @Override
    public void refreshUI(Meeting meeting) {
        setTitle(meeting.name);
        placeTextView.setText(meeting.place.name);
        addressTextView.setText(meeting.place.address);

        Date date = new Date(meeting.meetingDate);
        SimpleDateFormat spf = new SimpleDateFormat("yyyy. MM. dd. HH:mm");
        dateTextView.setText(spf.format(date));

        presenter.setStartButton();
    }

    @Override
    public void setAddedByTextView(String creator) {
        addedByTextView.setText(creator);
    }

    @Override
    public void setStartButtonFinished() {
        arrivedTextView.setText(presenter.getArrivedFriends());
        startButton.setText("Finished");
        startButton.setEnabled(false);
        startButton.setBackground(getResources().getDrawable(R.drawable.rounded_button_transparent));
        startButton.setTextColor( getResources().getColor(R.color.place_autocomplete_search_hint));
    }

    @Override
    public void setStartButtonNoTracking() {
        startButton.setText("Start");
        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(PermissionChecker.checkLocationPermission(getBaseContext())) {
                            Intent intent = new Intent(MeetingDetailActivity.this, TrackingService.class);
                            startService(intent);

                            presenter.startTracking();

                            finish();
                            startActivity(new Intent(MeetingDetailActivity.this, TrackingActivity.class));
                        }
                        else if (!PermissionChecker.checkLocationPermission(MeetingDetailActivity.this)) {
                            PermissionChecker.requestLocationPermission(MeetingDetailActivity.this);
                        }
                    }
                }
        );
    }

    @Override
    public void setStartButtonThisTrackingInProgress(boolean myTrackingStarted) {
        arrivedTextView.setText(presenter.getArrivedFriends());
        startButton.setText("Tracking");
        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: push notification ha még nem kezdte el de a többiek igen
                        Intent intent = new Intent(MeetingDetailActivity.this, TrackingActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void setStartButtonOtherTrackingInProgress() {

        startButton.setEnabled(false);
        startButton.setText("Start");
        startButton.setBackground(getResources().getDrawable(R.drawable.rounded_button_transparent));
        startButton.setTextColor( getResources().getColor(R.color.place_autocomplete_search_hint));
        //másik meeting tracking folyamatban
    }

    @Override
    public void setStartButtonExpired() {
        startButton.setText("Expired");
        startButton.setEnabled(false);
        startButton.setBackground(getResources().getDrawable(R.drawable.rounded_button_transparent));
        startButton.setTextColor( getResources().getColor(R.color.place_autocomplete_search_hint));
    }

    private void initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawer = new NavigationDrawer(drawerLayout, navigationView, MeetingDetailActivity.this);
        navigationDrawer.initNavigationDrawer();
        navigationView.getMenu().getItem(1).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.editMeeting:
                EditMeetingFragment editMeetingFragment = new EditMeetingFragment();
                editMeetingFragment.setMeeting(presenter.getMeeting());
                editMeetingFragment.show(getSupportFragmentManager(), EditMeetingFragment.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(presenter.canEdit())
            getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException se) {

        }

        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        Meeting meeting = presenter.getMeeting();

        LatLng placeLocation = new LatLng(meeting.place.latitude, meeting.place.longitude);
        map.addMarker(new MarkerOptions().position(placeLocation)
                .title(meeting.place.name));
        map.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
    }

    public void signOut() {
        presenter.signOut();
        Intent intent = new Intent(MeetingDetailActivity.this, AuthPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
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

    @Override
    public void onMeetingEdited(Meeting meeting) {
        presenter.setEditedMeeting(meeting);
    }
}
