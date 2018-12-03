package aut.bme.hu.friendsplus.ui.friends;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.friends.addFriend.NewFriendFragment;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerActivity;
import aut.bme.hu.friendsplus.util.NavigationDrawer;
import aut.bme.hu.friendsplus.util.RecyclerItemTouchHelper;
import aut.bme.hu.friendsplus.ui.listeners.NewFriendListener;
import aut.bme.hu.friendsplus.ui.listeners.RecyclerItemTouchHelperListener;

public class FriendsActivity extends BaseActivity implements NewFriendListener,
        RecyclerItemTouchHelperListener {

    public static final String TAG = "FriendsActivity";

    private RecyclerView recyclerView;
    private FriendsAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavigationDrawer navigationDrawer;
    private FriendsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setTitle("Friends");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        presenter = new FriendsPresenter();

        initRecyclerView();

        initItemTouchHelper();

        initDrawerLayout();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.friendsRecyclerView);
        adapter = new FriendsAdapter(presenter);

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
        navigationDrawer = new NavigationDrawer(drawerLayout, navigationView, FriendsActivity.this);
        navigationDrawer.initNavigationDrawer();
        navigationView.getMenu().getItem(2).setChecked(true);
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
                new NewFriendFragment().show(getSupportFragmentManager(), NewFriendFragment.TAG);
                return true;

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof FriendsAdapter.FriendRowViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();
            final String deletedUid = presenter.removeFriend(deletedIndex);

            showSnackbar("Friend is removed!", "UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    presenter.restoreFriend(deletedUid);
                    return;
                }
            });

        }
    }

    @Override
    public void signOut() {
        presenter.signOut();
        Intent intent = new Intent(FriendsActivity.this, AuthPickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();

        presenter.cleanupListener();
    }

    @Override
    public void onFriendCreated(User user) {
        presenter.addFriend(user.uid);
    }
}
