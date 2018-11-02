package aut.bme.hu.friendsplus.interactor.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class UserDatabaseInteractor {

    private DatabaseReference mDatabase;
    private UsersListener listener;

    public UserDatabaseInteractor(UsersListener listener){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;
    }

    public void writeNewUser(String uid, String name, String email) {
        User user = new User(name, email, uid);

        mDatabase.child("users").child(uid).setValue(user);

    }


    public void getUserByUid(final String uid) {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot friendsDataSnapshot : dataSnapshot.getChildren()) {
                    User user = friendsDataSnapshot.getValue(User.class);

                    if(user.uid.equals(uid)) {
                        listener.onUserFound(user);
                        return;
                    }
                }

                listener.onUserNotFound();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getUserByEmail(final String email) {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot friendsDataSnapshot : dataSnapshot.getChildren()) {
                    User user = friendsDataSnapshot.getValue(User.class);

                    if(user != null && user.email.equals(email)) {
                        listener.onUserFound(user);
                        return;
                    }
                }

                listener.onUserNotFound();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
