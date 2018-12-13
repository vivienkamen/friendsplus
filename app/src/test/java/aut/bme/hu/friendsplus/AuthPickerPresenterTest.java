package aut.bme.hu.friendsplus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerPresenter;
import aut.bme.hu.friendsplus.ui.authpicker.AuthPickerScreen;

public class AuthPickerPresenterTest {

    AuthPickerPresenter presenter;
    String result = "";

    @Before
    public void setUp() {
        presenter = new AuthPickerPresenter();

        AuthPickerScreen screen = new AuthPickerScreen() {
            @Override
            public void navigateToEmailLogin() {
                result = "email";
            }

            @Override
            public void navigateToMain() {
                result = "main";
            }

            @Override
            public void navigateToFacebookLogin() {
                result = "facebook";
            }

            @Override
            public void navigateToGoogleLogin() {
                result = "google";
            }
        };
        presenter.attachScreen(screen);
    }

    @Test
    public void testEmailLogin() {
        presenter.emailLogin();
        Assert.assertEquals("email", result);
    }

    @Test
    public void testFacebookLogin() {
        presenter.facebookLogin();
        Assert.assertEquals("facebook", result);
    }

    @Test
    public void testGoogleLogin() {
        presenter.googleLogin();
        Assert.assertEquals("google", result);
    }
}
