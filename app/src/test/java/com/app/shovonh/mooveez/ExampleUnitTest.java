package com.app.shovonh.mooveez;

import org.junit.Test;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void date(){

            DateTime dt = DateTime.now(TimeZone.getDefault());
            String start = dt.getStartOfMonth().format("YYYY-MM-DD");
            String end = dt.getEndOfMonth().format("YYYY-MM-DD");
            assertEquals("2016-05-01", start);
            assertEquals("2016-05-31", end);


    }
}