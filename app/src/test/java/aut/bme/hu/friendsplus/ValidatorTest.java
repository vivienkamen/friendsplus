package aut.bme.hu.friendsplus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import aut.bme.hu.friendsplus.util.Validator;

public class ValidatorTest {

    @Test
    public void testValidateDateUpcomingDate() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, 2);

        boolean valid = Validator.validateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        Assert.assertTrue(valid);
    }

    @Test
    public void testValidateDateExpiredDate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, 2017);

        boolean valid = Validator.validateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        Assert.assertFalse(valid);
    }

    @Test
    public void testValidateTimeUpcomingTime() {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.HOUR_OF_DAY, 1);

        boolean valid = Validator.validateTime(today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), today);
        Assert.assertTrue(valid);
    }

    @Test
    public void testValidateTimeExpiredTime() {
        Calendar today = Calendar.getInstance();
        today.roll(Calendar.HOUR_OF_DAY, -1);

        boolean valid = Validator.validateTime(today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), today);
        Assert.assertFalse(valid);
    }
}
