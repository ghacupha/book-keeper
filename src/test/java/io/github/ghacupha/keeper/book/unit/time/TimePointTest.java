package io.github.ghacupha.keeper.book.unit.time;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimePointTest {

    private TimePoint timePoint;

    @Before
    public void setUp() throws Exception {
        timePoint = new TimePoint(2018,5,12);
    }

    @Test
    public void addition() throws Exception {

        //assertEquals(timePoint.addDays(5),new TimePoint(2018,5,17));
        assertTrue(timePoint.addDays(5).equals(new TimePoint(2018,5,17)));
        assertTrue(timePoint.addDays(30).equals(new TimePoint(2018,6,11)));

    }

    @Test
    public void minusDays() throws Exception {

        assertTrue(timePoint.minusDays(5).equals(new TimePoint(2018,5,7)));
        assertTrue(timePoint.minusDays(30).equals(new TimePoint(2018,4,12)));
    }

    @Test
    public void after() throws Exception {
        assertTrue(timePoint.after(new TimePoint(2018,5,11)));
        assertTrue(timePoint.after(new TimePoint(2018,3,31)));
    }

    @Test
    public void before() throws Exception {
        assertTrue(timePoint.before(new TimePoint(2018,7,1)));
        assertTrue(timePoint.before(new TimePoint(2018,5,13)));
    }

    @Test
    public void newTimePoint() throws Exception {
        TimePoint testPoint = new TimePoint();

        assertTrue(new TimePoint().after(testPoint.minusDays(1)));
        assertTrue(new TimePoint().before(testPoint.addDays(1)));
    }

    @Test
    public void toStringTest() throws Exception {

        assertEquals("2018-05-08",new TimePoint(2018,5,8).toString());
    }
}