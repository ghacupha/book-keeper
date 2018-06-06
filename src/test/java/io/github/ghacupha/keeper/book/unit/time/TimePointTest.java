/*
 * Copyright © 2018 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ghacupha.keeper.book.unit.time;

import io.github.ghacupha.time.point.SimpleDate;
import io.github.ghacupha.time.point.TimePoint;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimePointTest {

    private TimePoint timePoint;

    @Before
    public void setUp() throws Exception {
        timePoint = new SimpleDate(2018,5,12);
    }

    @Test
    public void addition() throws Exception {

        //assertEquals(timePoint.addDays(5),new SimpleDate(2018,5,17));
        assertTrue(timePoint.addDays(5).equals(new SimpleDate(2018,5,17)));
        assertTrue(timePoint.addDays(30).equals(new SimpleDate(2018,6,11)));

    }

    @Test
    public void minusDays() throws Exception {

        assertTrue(timePoint.minusDays(5).equals(new SimpleDate(2018,5,7)));
        assertTrue(timePoint.minusDays(30).equals(new SimpleDate(2018,4,12)));
    }

    @Test
    public void after() throws Exception {
        assertTrue(timePoint.after(new SimpleDate(2018,5,11)));
        assertTrue(timePoint.after(new SimpleDate(2018,3,31)));
    }

    @Test
    public void before() throws Exception {
        assertTrue(timePoint.before(new SimpleDate(2018,7,1)));
        assertTrue(timePoint.before(new SimpleDate(2018,5,13)));
    }

    @Test
    public void newTimePoint() throws Exception {
        TimePoint testPoint = new SimpleDate();

        assertTrue(new SimpleDate().after(testPoint.minusDays(1)));
        assertTrue(new SimpleDate().before(testPoint.addDays(1)));
    }

    @Test
    public void toStringTest() throws Exception {

        assertEquals("2018-05-08",new SimpleDate(2018,5,8).toString());
    }
}