/*
 *  Copyright 2018 Edwin Njeru
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.ghacupha.keeper.book.unit.time;

import java.time.LocalDate;

/**
 * A representation of a point in time to the accuracy of a day
 *
 * @author edwin.njeru
 */
public class SimpleDate implements TimePoint {

    static final SimpleDate PAST = new SimpleDate(LocalDate.MIN);
    static final SimpleDate FUTURE = new SimpleDate(LocalDate.MAX);

    private LocalDate base;

    private SimpleDate(LocalDate arg) {
        initialize(arg);
    }

    public SimpleDate(int year, int month, int day) {
        initialize(LocalDate.of(year, month, day));
    }

    public SimpleDate() {
        initialize(LocalDate.now());
    }

    public static TimePoint newMoment(int year, int month, int day) {
        return new SimpleDate(year, month, day);
    }

    public static TimePoint now() {

        return new SimpleDate();
    }

    public static TimePoint on(int year, int month, int dayOfMonth) {

        return new SimpleDate(year, month, dayOfMonth);
    }

    private void initialize(LocalDate arg) {
        this.base = arg;
    }

    @Override
    public boolean after(TimePoint arg) {
        return this.base.isAfter(getDay(arg));
    }

    @Override
    public boolean before(TimePoint arg) {
        return this.base.isBefore(getDay(arg));
    }

    @Override
    public SimpleDate addDays(int arg) {
        return new SimpleDate(this.base.plusDays(arg));
    }

    @Override
    public SimpleDate minusDays(int arg) {
        return new SimpleDate(this.base.minusDays(arg));
    }

    @Override
    public int compareTo(TimePoint arg) {
        SimpleDate other = (SimpleDate) arg;
        return this.base.compareTo(other.base);
    }

    @Override
    public boolean equals(Object arg) {
        if (!(arg instanceof SimpleDate)) {
            return false;
        }
        SimpleDate other = (SimpleDate) arg;
        return (base.equals(other.base));
    }

    @Override
    public int hashCode() {
        return base != null ? base.hashCode() : 0;
    }

    @Override
    public String toString() {
        return base.toString();
    }

    private LocalDate getDay(TimePoint arg) {
        SimpleDate simpleDate = (SimpleDate) arg;
        return simpleDate.base;
    }
}
