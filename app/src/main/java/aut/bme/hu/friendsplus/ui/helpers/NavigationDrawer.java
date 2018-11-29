package aut.bme.hu.friendsplus.ui.helpers;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import aut.bme.hu.friendsplus.R;
import aut.bme.hu.friendsplus.ui.BaseActivity;
import aut.bme.hu.friendsplus.ui.account.AccountActivity;
import aut.bme.hu.friendsplus.ui.friends.FriendsActivity;
import aut.bme.hu.friendsplus.ui.meetings.MeetingsActivity;
import aut.bme.hu.friendsplus.ui.messages.MessagesOverviewActivity;
import aut.bme.hu.friendsplus.ui.tracking.TrackingActivity;

public class NavigationDrawer {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BaseActivity activity;

    public NavigationDrawer(DrawerLayout drawerLayout, NavigationView navigationView, BaseActivity activity) {
        this.drawerLayout = drawerLayout;
        this.navigationView = navigationView;
        this.activity = activity;
    }

    public void initNavigationDrawer() {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);

                        drawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.account:
                                Intent intentAccount = new Intent(activity, AccountActivity.class);
                                intentAccount.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // || single top todo
                                activity.startActivity(intentAccount);
                                break;
                            case R.id.meetings:
                                Intent intentMeetings = new Intent(activity, MeetingsActivity.class);
                                intentMeetings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intentMeetings);
                                break;
                            case R.id.friends:
                                Intent intentFriends = new Intent(activity, FriendsActivity.class);
                                intentFriends.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intentFriends);
                                break;
                            case R.id.tracking:
                                Intent intentTracking = new Intent(activity, TrackingActivity.class);
                                intentTracking.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intentTracking);
                                break;
                            case R.id.messages:
                                Intent intentMessages = new Intent(activity, MessagesOverviewActivity.class);
                                intentMessages.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intentMessages);
                                break;
                            case R.id.signOut:
                                activity.signOut();
                                break;

                        }

                        return true;
                    }
                });

    }

}

