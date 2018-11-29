package aut.bme.hu.friendsplus.ui.messages;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.Meeting;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;
import aut.bme.hu.friendsplus.ui.helpers.NavigationDrawer;
import aut.bme.hu.friendsplus.ui.helpers.RecyclerItemTouchHelper;
import aut.bme.hu.friendsplus.ui.listeners.ItemClickListener;
import aut.bme.hu.friendsplus.ui.listeners.MessagingStartedListener;
import aut.bme.hu.friendsplus.ui.listeners.RecyclerItemTouchHelperListener;
import aut.bme.hu.friendsplus.ui.meetings.MeetingsActivity;
import aut.bme.hu.friendsplus.ui.meetings.MeetingsAdapter;
import aut.bme.hu.friendsplus.ui.meetings.addMeeting.NewMeetingFragment;

public class MessagesOverviewActivity extends BaseActivity implements ItemClickListener<User>,
        MessagingStartedListener, RecyclerItemTouchHelperListener {

    public static final String TAG = "MessagesOverviewActivity";

    private RecyclerView recyclerView;
    private MessagesOverviewAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawer navigationDrawer;
    private MessagesOverviewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_overview);

        setTitle("Messages");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        presenter = new MessagesOverviewPresenter();

        initRecyclerView();
        initDrawerLayout();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.meetingsRecyclerView);
        adapter = new MessagesOverviewAdapter(presenter, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initItemTouchHelper() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, RecyclerItemTouchHelper.MEETING_FLAG);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationDrawer = new NavigationDrawer(drawerLayout, navigationView, MessagesOverviewActivity.this);
        navigationDrawer.initNavigationDrawer();
        navigationView.getMenu().getItem(4).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case  R.id.AddItem:
                //new NewMeetingFragment().show(getSupportFragmentManager(), NewMeetingFragment.TAG);
                return true;

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onItemClick(User user) {

    }
    @Override
    public void onStop() {
        super.onStop();

        presenter.cleanupListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refreshItems();
    }

    public void signOut() {
        presenter.signOut();
        Intent intent = new Intent(MessagesOverviewActivity.this, AuthPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onMessagingStarted() {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof MessagesOverviewAdapter.MessageRowViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();
            presenter.removeMessagesFromFriend(deletedIndex);

        }
    }
}
