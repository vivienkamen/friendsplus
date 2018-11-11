package aut.bme.hu.friendsplus.interactor.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import aut.bme.hu.friendsplus.ui.listeners.AuthListener;
import aut.bme.hu.friendsplus.ui.listeners.FirebaseUserListener;


public class AuthInteractor {

    private static final String TAG ="AuthInteractor";
    private FirebaseAuth mAuth;
    private AuthListener authListener;
    private FirebaseUserListener firebaseUserListener;

    public AuthInteractor(AuthListener authListener, FirebaseUserListener firebaseUserListener) {
        mAuth = FirebaseAuth.getInstance();
        this.authListener = authListener;
        this.firebaseUserListener = firebaseUserListener;
    }

    public boolean isLoggedIn() {
        if (mAuth.getCurrentUser() != null) {
            return true;
        }
        return false;
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            authListener.onSuccess(null);

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            authListener.onFailure("Authentication failed."
                                    + task.getException().getMessage());
                        }


                    }
                });

    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");

                            authListener.onSuccess(task.getResult().getUser().getUid());


                        } else {

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {

                                authListener.onFailure("E-mail is already registered.");

                            } else {

                                authListener.onFailure(task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            authListener.onSuccess(mAuth.getCurrentUser().getUid());

                        } else {

                            authListener.onFailure("Google Authentication Failed.");
                        }
                    }
                });
    }

    public void firebaseAuthWithFacebook(AuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            authListener.onSuccess(mAuth.getCurrentUser().getUid());
                        } else {

                            authListener.onFailure("Facebook Authentication failed.");
                        }
                    }
                });

    }

    public void signOut() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void updateFirebaseUser(String email) {
        FirebaseUser firebaseUser = getCurrentUser();

        if(firebaseUser != null) {
            firebaseUser.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                                firebaseUserListener.onUserUpdated();
                            }
                        }
                    });
        }
    }


}
