package aut.bme.hu.friendsplus.ui.login.google;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;

public class GoogleLoginPresenter extends Presenter<GoogleLoginScreen> implements AuthListener {

    AuthInteractor authInteractor;
    UserDatabaseInteractor userDatabaseInteractor;

    public GoogleLoginPresenter() {
        authInteractor = new AuthInteractor(this, null);
        userDatabaseInteractor = new UserDatabaseInteractor(null);
    }

    @Override
    public void attachScreen(GoogleLoginScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    @Override
    public void onSuccess(String message) {
        userDatabaseInteractor.writeNewUser(message, " ", authInteractor.getCurrentUser().getEmail());
        screen.showSuccessfulLogin();
        screen.checkPermission();
        screen.navigateToMain();
    }

    @Override
    public void onFailure(String message) {
        screen.showLoginFailure(message);
        screen.navigateBack();
    }

    public void googleLogin(GoogleSignInAccount acct) {
        authInteractor.firebaseAuthWithGoogle(acct);
    }
}
