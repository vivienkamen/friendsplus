package aut.bme.hu.friendsplus.interactor.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.listeners.UserListListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class UserDatabaseInteractor {

    private DatabaseReference mDatabase;
    private UsersListener usersListener;
    private UserListListener userListListener;

    public UserDatabaseInteractor(UsersListener usersListener, UserListListener userListListener){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.usersListener = usersListener;
        this.userListListener = userListListener;
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
                        usersListener.onUserFound(user);
                        return;
                    }
                }

                usersListener.onUserNotFound();
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
                        usersListener.onUserFound(user);
                        return;
                    }
                }

                usersListener.onUserNotFound();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getUsers(final List<String> uids) {
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for(DataSnapshot friendsDataSnapshot : dataSnapshot.getChildren()) {
                    User user = friendsDataSnapshot.getValue(User.class);
                    if(uids.contains(user.uid)) {
                        users.add(user);
                    }
                }
                userListListener.onUserListReady(users);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void updateUserDatabase(User user) {
        Map<String, Object> userValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + user.uid, userValues);

        mDatabase.updateChildren(childUpdates);
    }
}
