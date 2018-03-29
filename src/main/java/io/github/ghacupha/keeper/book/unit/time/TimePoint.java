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

/**
 * This is used and library-specific representation of time for the library.
 * The granularity of the implementation is set to days.
 * The implementation is also such that this remains immutable. For instance if days are
 * added to this, it will remain the same unless it is newly reassigned
 *
 * @author edwin.njeru
 */
public interface TimePoint extends Comparable<TimePoint> {

    /**
     * @param arg SimpleDate by which we compare this
     * @return True if the moment is after this
     */
    boolean after(TimePoint arg);

    /**
     * @param arg SimpleDate by which we compare this
     * @return True if the moment is before this
     */
    boolean before(TimePoint arg);

    /**
     * Adds days to this
     *
     * @param arg Number of days to be added
     * @return new instance of {@link TimePoint} with additional days
     */
    TimePoint addDays(int arg);

    /**
     * Removes days from this
     *
     * @param arg Number of days to be subtracted
     * @return new instance of {@link TimePoint} with days less the argument
     */
    TimePoint minusDays(int arg);

    @Override
    boolean equals(Object arg);

    @Override
    int hashCode();

    @Override
    String toString();
}
