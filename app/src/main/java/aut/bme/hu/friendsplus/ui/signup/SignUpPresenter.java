package aut.bme.hu.friendsplus.ui.signup;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;

public class SignUpPresenter extends Presenter<SignUpScreen> implements AuthListener {

    AuthInteractor authInteractor;
    UserDatabaseInteractor userDatabaseInteractor;

    public SignUpPresenter() {
        authInteractor = new AuthInteractor(this, null);
        userDatabaseInteractor = new UserDatabaseInteractor(null, null);
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
        userDatabaseInteractor.writeNewUser(uid, screen.getUserName(), screen.getEmail());
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
