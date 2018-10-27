package aut.bme.hu.friendsplus.interactor.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aut.bme.hu.friendsplus.model.User;

public class DatabaseInteractor {

    private DatabaseReference mDatabase;

    public DatabaseInteractor(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewUser(String uid, String name, String email) {
        User user = new User(name, email, uid);

        mDatabase.child("users").child(uid).setValue(user);

    }
}
