package aut.bme.hu.friendsplus.ui.listeners;

import com.google.firebase.database.DataSnapshot;

public interface LocationListener {
    void onLocationAddedOrChanged(DataSnapshot dataSnapshot);
}
