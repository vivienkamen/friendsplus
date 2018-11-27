package aut.bme.hu.friendsplus.ui.account;

import android.net.Uri;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.interactor.database.UserDatabaseInteractor;
import aut.bme.hu.friendsplus.interactor.storage.StorageInteractor;
import aut.bme.hu.friendsplus.model.User;
import aut.bme.hu.friendsplus.ui.Presenter;
import aut.bme.hu.friendsplus.ui.listeners.FirebaseUserListener;
import aut.bme.hu.friendsplus.ui.listeners.StorageListener;
import aut.bme.hu.friendsplus.ui.listeners.UsersListener;

public class AccountPresenter extends Presenter<AccountScreen> implements UsersListener, StorageListener,
        FirebaseUserListener {

    AuthInteractor authInteractor;
    UserDatabaseInteractor userDatabaseInteractor;
    StorageInteractor storageInteractor;

    public AccountPresenter() {

        authInteractor = new AuthInteractor();
        userDatabaseInteractor = new UserDatabaseInteractor();
        storageInteractor = new StorageInteractor();

        authInteractor.setFirebaseUserListener(this);
        userDatabaseInteractor.setUsersListener(this);
        storageInteractor.setStorageListener(this);
    }

    @Override
    public void attachScreen(AccountScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public void saveUserInformation() {
        Uri uri = screen.getProfileImageUri();
        if(uri != null) {
            storageInteractor.saveImage(authInteractor.getCurrentUser().getUid(), uri);
        } else {
            screen.setUpdatedUser();
            screen.updateCurrentUser();
            authInteractor.updateFirebaseEmail(screen.getUpdatedUser().email);
            userDatabaseInteractor.updateUserDatabase(screen.getUpdatedUser());
        }
        screen.resetUpdatedUser();
    }


    public void signOut() {
        authInteractor.signOut();
    }

    @Override
    public void onUserFound(User user) {
        screen.setCurrentUser(user);
        screen.initUI();
        screen.updateUI();
    }

    @Override
    public void onUserNotFound() {}

    @Override
    public void onImageSaved(String imageUri) {
        screen.setImageUri(imageUri);
        screen.setUpdatedUser();
        screen.updateCurrentUser();
        authInteractor.updateFirebaseEmail(screen.getUpdatedUser().email);
        userDatabaseInteractor.updateUserDatabase(screen.getUpdatedUser());
    }

    public void setCurrentUser() {
        userDatabaseInteractor.getUserByUid(authInteractor.getCurrentUser().getUid());
    }

    @Override
    public void onUserUpdated() {
        screen.updateUI();
        screen.showSuccessfulUpdate();
    }
}
