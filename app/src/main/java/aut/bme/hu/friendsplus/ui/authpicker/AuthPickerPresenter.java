package aut.bme.hu.friendsplus.ui.authpicker;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;

public class AuthPickerPresenter extends Presenter<AuthPickerScreen> {

    AuthInteractor authInteractor;

    public AuthPickerPresenter() {
        authInteractor = new AuthInteractor();

    }

    @Override
    public void attachScreen(AuthPickerScreen screen) {
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

    public void facebookLogin() {screen.navigateToFacebookLogin();}

    public void googleLogin() {
        screen.navigateToGoogleLogin();
    }

    public void emailLogin() {screen.navigateToEmailLogin();}

}
