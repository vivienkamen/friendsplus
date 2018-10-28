package aut.bme.hu.friendsplus.ui.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.;

public class MeetingsActivity extends BaseActivity implements View.OnClickListener, MeetingsAdapter.ItemClickListener{

    public static final String TAG = "MeetingsActivity";

    private RecyclerView recyclerView;
    private MeetingsAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationDrawer navigationDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);

        setTitle("Meetings");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.placesRecyclerView);
        adapter = new MeetingsAdapter(this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onItemClick(Meeting meeting, int position) {

    }
}
