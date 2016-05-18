package com.app.shovonh.mooveez;

import org.junit.Assert;
import org.junit.Test;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTests {
    @Test
    public void addition_isCorrect() throws Exception {
        Assert.assertEquals(4, 2 + 2);
    }

    @Test
    public void date(){

            DateTime dt = DateTime.now(TimeZone.getDefault());
            String start = dt.getStartOfMonth().format("YYYY-MM-DD");
            String end = dt.getEndOfMonth().format("YYYY-MM-DD");
            Assert.assertEquals("2016-05-01", start);
            Assert.assertEquals("2016-05-31", end);
    }

    @Test
    public void release(){
        String a = Utilities.dateFormatter("2016-05-14");
        String b = Utilities.dateFormatter("2016-05-12");
        String c = Utilities.dateFormatter("2016-05-13");

        Assert.assertEquals("Releasing on the 14th", a);
        Assert.assertEquals("Released on the 12th", b);
        Assert.assertEquals("Releasing today!", c);

    }

    @Test
    public void firstOfMonth(){
        DateTime dt = DateTime.today(TimeZone.getDefault());
        String end = dt.getEndOfMonth().format("MM-DD-YYYY");
        Assert.assertEquals("05-31-2016", end);
        String firstOfNext = dt.getEndOfMonth().plusDays(1).format("MM-DD-YYYY");
        Assert.assertEquals("06-01-2016", firstOfNext);
        DateTime dt2 = new DateTime("2016-05-18 06:00:00");
        Assert.assertEquals(1463565600000L, dt2.getMilliseconds(TimeZone.getDefault()));

    }
}