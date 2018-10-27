package aut.bme.hu.friendsplus.ui.signup;

import com.google.firebase.auth.FirebaseUser;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.DatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;

public class SignUpPresenter extends Presenter<SignUpScreen> implements AuthListener {

    AuthInteractor authInteractor;
    DatabaseInteractor databaseInteractor;

    public SignUpPresenter() {
        authInteractor = new AuthInteractor(this);
        databaseInteractor = new DatabaseInteractor();
    }

    @Override
    public void attachScreen(SignUpScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void signUp(String email, String password) {
        authInteractor.createAccount(email, password);
    }

    public void registerNewUser(String uid) {
        databaseInteractor.writeNewUser(uid, screen.getUserName(), screen.getEmail());
    }

    @Override
    public void onSuccess(String message) {
        registerNewUser(message);

        screen.showSignUpSuccess();
        screen.navigateToLogin();
    }

    @Override
    public void onFailure(String message) {
        screen.showSignUpFailure(message);
    }
}
