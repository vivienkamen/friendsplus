package aut.bme.hu.friendsplus.ui.login.google;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.DatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;
import aut.bme.hu.friendsplus.ui.login.LoginScreen;

public class GoogleLoginPresenter extends Presenter<GoogleLoginScreen> implements AuthListener {

    AuthInteractor authInteractor;
    DatabaseInteractor databaseInteractor;

    public GoogleLoginPresenter() {
        authInteractor = new AuthInteractor(this);
        databaseInteractor = new DatabaseInteractor();
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
        databaseInteractor.writeNewUser(message, " ", authInteractor.getCurrentUser().getEmail());
        screen.showSuccessfulLogin();
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
