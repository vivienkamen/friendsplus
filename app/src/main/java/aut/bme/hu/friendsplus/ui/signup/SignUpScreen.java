package aut.bme.hu.friendsplus.ui.signup;

public interface SignUpScreen {
    void navigateToLogin();
    void showSignUpSuccess();
    void showSignUpFailure(String message);
    String getUserName();
    String getEmail();
}
