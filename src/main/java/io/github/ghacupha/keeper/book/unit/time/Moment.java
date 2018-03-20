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
public class Moment implements TimePoint {

    static final Moment PAST = new Moment(LocalDate.MIN);
    static final Moment FUTURE = new Moment(LocalDate.MAX);

    private LocalDate base;

    private Moment(LocalDate arg) {
        initialize(arg);
    }

    public Moment(int year, int month, int day) {
        initialize(LocalDate.of(year, month, day));
    }

    public Moment() {
        initialize(LocalDate.now());
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
    public Moment addDays(int arg) {
        return new Moment(this.base.plusDays(arg));
    }

    @Override
    public Moment minusDays(int arg) {
        return new Moment(this.base.minusDays(arg));
    }

    @Override
    public int compareTo(Object arg) {
        Moment other = (Moment) arg;
        return this.base.compareTo(other.base);
    }

    @Override
    public boolean equals(Object arg) {
        if (!(arg instanceof Moment)) {
            return false;
        }
        Moment other = (Moment) arg;
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
        Moment moment = (Moment) arg;
        return moment.base;
    }
}
