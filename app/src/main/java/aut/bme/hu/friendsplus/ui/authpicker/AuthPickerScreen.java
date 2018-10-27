package aut.bme.hu.friendsplus.ui.authpicker;

public interface AuthPickerScreen {
    void navigateToEmailLogin();
    void navigateToMain();
    void navigateToFacebookLogin();
    void navigateToGoogleLogin();
    void showSuccessfulLogin();
    void showLoginFailure(String message);
}
