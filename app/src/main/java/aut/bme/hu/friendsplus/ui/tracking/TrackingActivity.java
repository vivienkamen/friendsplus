package aut.bme.hu.friendsplus.ui.tracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.services.TrackingService;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;
import aut.bme.hu.friendsplus.ui.helpers.NavigationDrawer;
import aut.bme.hu.friendsplus.ui.helpers.PermissionChecker;
import aut.bme.hu.friendsplus.ui.listeners.TrackedMeetingsListener;
import aut.bme.hu.friendsplus.ui.meetingDetails.MeetingDetailActivity;

public class TrackingActivity extends BaseActivity implements TrackingScreen, OnMapReadyCallback {

    private static final String TAG = "TrackingActivity";

    TrackingPresenter presenter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawer navigationDrawer;
    private Button doneButton;
    private MapFragment mapFragment;
    private GoogleMap map;
    private TextView noTrackingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        presenter = new TrackingPresenter();
        presenter.attachScreen(this);

        initUI();
    }


    @Override
    protected void onStop() {
        presenter.detachScreen();
        presenter.cleanUpLocationListener();
        super.onStop();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        initDrawerLayout();

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setVisibility(View.GONE);
        noTrackingTextView = (TextView) findViewById(R.id.noTrackingTextView);

        presenter.setMeeting((Meeting) getIntent().getParcelableExtra("Meeting"));

    }

    private void initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawer = new NavigationDrawer(drawerLayout, navigationView, TrackingActivity.this);
        navigationDrawer.initNavigationDrawer();
        navigationView.getMenu().getItem(3).setChecked(true);

    }

    @Override
    public void setTrackingUI(Meeting meeting) {
        setTitle(meeting.name);
        noTrackingTextView.setVisibility(View.GONE);

    }

    @Override
    public void setNoTrackingUI() {
        setTitle("Tracking");
        noTrackingTextView.setText("No tracked meeting");
    }

    @Override
    public void setStartButton() {
        doneButton.setVisibility(View.VISIBLE);
        doneButton.setText("Start");
        doneButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent serviceIntent = new Intent(TrackingActivity.this, TrackingService.class);
                        startService(serviceIntent);

                        setDoneButton();
                    }
                }
        );
    }

    @Override
    public void setDoneButton() {
        doneButton.setVisibility(View.VISIBLE);
        doneButton.setText("Done");
        doneButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        presenter.setArrival();

                        stopService(new Intent(TrackingActivity.this, TrackingService.class));

                        Intent intent = new Intent(TrackingActivity.this, MeetingDetailActivity.class);
                        intent.putExtra("Meeting", presenter.getMeeting());
                        finish();
                        startActivity(intent);

                    }
                }
        );
    }

    @Override
    public void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Notification");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setStartButton();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showMeetingPickerDialog(final ArrayList<Meeting> meetingList) {
        TrackedMeetingPickerAdapter adapter = new TrackedMeetingPickerAdapter(this, meetingList);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select meeting")
                .setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        presenter.setMeeting(meetingList.get(index));
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });;

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public Marker addMarkerToMap(LatLng placeLocation, String username) {
        return map.addMarker(new MarkerOptions().position(placeLocation).title(username));
    }

    @Override
    public void setMapCamera(LatLng placeLocation) {
        map.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 1000, null);
    }

    @Override
    public void addMeetingMarker(Meeting meeting) {
        if(meeting != null && map != null) {
            LatLng placeLocation = new LatLng(meeting.place.latitude, meeting.place.longitude);
            map.addMarker(new MarkerOptions().position(placeLocation)
                    .title("Destination").snippet(meeting.place.name));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.meetingDetail:
                Intent intent = new Intent(TrackingActivity.this, MeetingDetailActivity.class);
                intent.putExtra("Meeting", presenter.getMeeting());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(presenter.getMeeting() != null) {
            getMenuInflater().inflate(R.menu.tracking_menu, menu);
        }

        return true;
    }

    public void signOut() {
        presenter.signOut();
        Intent intent = new Intent(TrackingActivity.this, AuthPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException se) { }

        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        addMeetingMarker(presenter.getMeeting());
    }
}
