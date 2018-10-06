package com.gvjay.classmanager;

import android.text.format.DateUtils;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilsTest {
    @Test
    public void testGetTiming(){
        long fromTime = 13*DateUtils.HOUR_IN_MILLIS + 30*DateUtils.MINUTE_IN_MILLIS;
        long toTime = 14*DateUtils.HOUR_IN_MILLIS + 9*DateUtils.MINUTE_IN_MILLIS;

        String expected = "1:30-2:09 PM";
        assertEquals(expected, Utils.getTiming(fromTime, toTime));

        fromTime = 10*DateUtils.HOUR_IN_MILLIS + 30*DateUtils.MINUTE_IN_MILLIS;
        toTime = 12*DateUtils.HOUR_IN_MILLIS + 9*DateUtils.MINUTE_IN_MILLIS;

        expected = "10:30 AM-12:09 PM";
        assertEquals(expected, Utils.getTiming(fromTime, toTime));
    }
}
