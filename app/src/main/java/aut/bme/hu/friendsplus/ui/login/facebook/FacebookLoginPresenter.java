package aut.bme.hu.friendsplus.ui.login.facebook;

import com.facebook.AccessToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.AuthListener;

public class FacebookLoginPresenter extends Presenter<FacebookLoginScreen> implements AuthListener {

    private static final String TAG = "FacebookLoginPresenter";
    AuthInteractor authInteractor;
    UserDatabaseInteractor userDatabaseInteractor;

    public FacebookLoginPresenter() {
        authInteractor = new AuthInteractor(this);
        userDatabaseInteractor = new UserDatabaseInteractor(null);
    }

    @Override
    public void attachScreen(FacebookLoginScreen screen) {
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


    public void loginToFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        authInteractor.firebaseAuthWithFacebook(credential);
    }

}
