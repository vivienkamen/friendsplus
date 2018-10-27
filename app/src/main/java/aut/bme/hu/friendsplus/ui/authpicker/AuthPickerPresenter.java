package aut.bme.hu.friendsplus.ui.authpicker;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.DatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;

public class AuthPickerPresenter extends Presenter<AuthPickerScreen> implements AuthListener {

    AuthInteractor authInteractor;
    DatabaseInteractor databaseInteractor;

    public AuthPickerPresenter() {
        authInteractor = new AuthInteractor(this);
        databaseInteractor = new DatabaseInteractor();
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
        databaseInteractor.writeNewUser(message, " ", authInteractor.getCurrentUser().getEmail());
        screen.showSuccessfulLogin();
        screen.navigateToMain();
    }

    @Override
    public void onFailure(String message) {
        screen.showLoginFailure(message);
    }

}
