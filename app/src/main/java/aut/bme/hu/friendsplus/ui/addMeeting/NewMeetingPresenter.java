package aut.bme.hu.friendsplus.ui.addMeeting;

import aut.bme.hu.friendsplus.interactor.auth.AuthInteractor;
import aut.bme.hu.friendsplus.ui.Presenter;

public class NewMeetingPresenter extends Presenter<NewMeetingScreen> {

    private AuthInteractor authInteractor;

    public NewMeetingPresenter() {
        authInteractor = new AuthInteractor(null, null);
    }

    @Override
    public void attachScreen(NewMeetingScreen screen) {
        super.attachScreen(screen);
    }

    @Override
    public void detachScreen() {
        super.detachScreen();
    }

    public String getUID() {
        return authInteractor.getCurrentUser().getUid();
    }
}
