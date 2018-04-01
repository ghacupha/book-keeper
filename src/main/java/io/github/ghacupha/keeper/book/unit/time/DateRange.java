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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Represents a range {@link SimpleDate} objects of {@link TimePoint} start, and {@link TimePoint} end
 * <p>
 * adopted from Martin Fowler's Range implementation in {@literal https://martinfowler.com/eaaDev/Range.html}
 *
 * @author edwin.njeru
 */
public class DateRange implements Comparable {

    private static final Logger log = LoggerFactory.getLogger(DateRange.class);
    /**
     * Empty DateRange created by providing any end date that is sooner than the start date
     */
    private static final DateRange empty = new DateRange(new SimpleDate(2000, 4, 1), new SimpleDate(2000, 1, 1));
    private TimePoint start;
    private TimePoint end;

    public DateRange(TimePoint start, TimePoint end) {
        this.start = start;
        this.end = end;
    }

    public DateRange(LocalDate start, LocalDate end) {
        this(new SimpleDate(start.getYear(), start.getMonthValue(), start.getDayOfMonth()), new SimpleDate(end.getYear(), end.getMonthValue(), end.getDayOfMonth()));
    }

    // open ended constructor
    public static DateRange upTo(SimpleDate end) {
        return new DateRange(SimpleDate.PAST, end);
    }

    public static DateRange startingOn(SimpleDate start) {
        return new DateRange(start, SimpleDate.FUTURE);
    }

    public static DateRange combination(DateRange[] args) {

        Arrays.sort(args);

        if (!isContiguous(args)) {
            throw new IllegalArgumentException("Unable to combine dateRanges");
        }
        return new DateRange(args[0].start, args[args.length - 1].end);
    }

    public static boolean isContiguous(DateRange[] args) {

        Arrays.sort(args);

        for (int i = 0; i < args.length - 1; i++) {

            if (!args[i].abuts(args[i + 1])) {
                return false;
            }
        }

        return true;
    }

    public TimePoint getEnd() {
        return end;
    }

    public TimePoint getStart() {
        return start;
    }

    public boolean includes(TimePoint arg) {
        log.trace("Checking if : {} includes timePoint : {}", this, arg);
        return !arg.before(start) && !arg.after(end);
    }

    private boolean isEmpty() {

        return start.after(end);
    }

    public boolean overlaps(DateRange arg) {
        return arg.includes(start) || arg.includes(end) || this.includes(arg);
    }

    public boolean includes(DateRange arg) {
        return this.includes(arg.getStart()) && this.includes(arg.getEnd());
    }

    public DateRange gap(DateRange arg) {
        if (this.overlaps(arg)) {
            return DateRange.empty;
        }
        DateRange lower, higher;

        if (this.compareTo(arg) < 0) {
            lower = this;
            higher = arg;
        } else {
            lower = arg;
            higher = this;
        }

        return new DateRange(lower.end.addDays(1), higher.start.addDays(-1));
    }

    public boolean abuts(DateRange arg) {
        return !this.overlaps(arg) && this.gap(arg).isEmpty();
    }

    public boolean partitionedBy(DateRange[] args) {
        if (!isContiguous(args)) {
            return false;
        }
        return this.equals(DateRange.combination(args));
    }

    @Override
    public int compareTo(Object arg) {
        DateRange other = (DateRange) arg;
        if (!start.equals(other.start)) {
            return start.compareTo(other.start);
        }
        return end.compareTo(other.end);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Empty Date Range";
        }
        return start.toString() + " - " + end.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DateRange)) {
            return false;
        }
        DateRange other = (DateRange) obj;

        return start.equals(other.getStart()) && end.equals(other.getEnd());
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }
}
