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

import io.github.ghacupha.time.point.DateRange;
import io.github.ghacupha.time.point.SimpleDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateRangeTest {

    private DateRange dateRange;

    @Before
    public void setUp() throws Exception {
        dateRange = new DateRange(SimpleDate.newMoment(2017, 9, 30), SimpleDate.newMoment(2017, 12, 30));
    }

    @Test
    public void upTo() {

        assertTrue(dateRange.includes(SimpleDate.newMoment(2017, 11, 30)));
        assertTrue(dateRange.includes(SimpleDate.newMoment(2017, 12, 30)));
        assertFalse(dateRange.includes(SimpleDate.newMoment(2017, 12, 31)));

        DateRange infiniteStart = DateRange.upTo((SimpleDate) SimpleDate.newMoment(2017, 11, 30));
        DateRange infiniteEnd = DateRange.startingOn((SimpleDate) SimpleDate.newMoment(2017, 11, 30));

        assertTrue(infiniteStart.includes(SimpleDate.newMoment(1900, 01, 01)));
        assertTrue(infiniteEnd.includes(SimpleDate.newMoment(9999, 01, 01)));
    }


}