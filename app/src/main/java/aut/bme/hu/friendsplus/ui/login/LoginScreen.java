package aut.bme.hu.friendsplus.ui.login;

public interface LoginScreen {
    void navigateToMain();
    void showLoginFailure(String message);
    void showSuccessfulLogin();
}
