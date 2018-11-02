package aut.bme.hu.friendsplus.ui.login.email;

import com.google.firebase.auth.FirebaseUser;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;
import aut.bme.hu.friendsplus.ui.login.LoginScreen;

public class EmailLoginPresenter extends Presenter<LoginScreen> implements AuthListener {

    AuthInteractor authInteractor;

    public EmailLoginPresenter() {
        authInteractor = new AuthInteractor(this);
    }

    @Override
    public void attachScreen(LoginScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void checkIfLoggedIn() {
        if (authInteractor.isLoggedIn()) {
            screen.navigateToMain();
        }
    }

    public void login(String email, String password) {
        authInteractor.login(email,password);
    }

    @Override
    public void onSuccess(String message) {
        screen.showSuccessfulLogin();
        screen.checkPermission();
        screen.navigateToMain();
    }

    @Override
    public void onFailure(String message) {
        screen.showLoginFailure(message);
    }
}
