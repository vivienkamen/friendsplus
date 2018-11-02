package aut.bme.hu.friendsplus.ui.authpicker;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;

public class AuthPickerPresenter extends Presenter<AuthPickerScreen> implements AuthListener {

    AuthInteractor authInteractor;
    UserDatabaseInteractor userDatabaseInteractor;

    public AuthPickerPresenter() {
        authInteractor = new AuthInteractor(this);
        userDatabaseInteractor = new UserDatabaseInteractor(null);
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

    @Override
    public void onSuccess(String message) {
        userDatabaseInteractor.writeNewUser(message, " ", authInteractor.getCurrentUser().getEmail());
        screen.showSuccessfulLogin();
        screen.navigateToMain();
    }

    @Override
    public void onFailure(String message) {
        screen.showLoginFailure(message);
    }

}
