/*******************************************************************************
 * Copyright 2018 edwin.njeru
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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

package io.github.ghacupha.keeper.book.unit.money;

import java.math.RoundingMode;

/**
 * Implementation of the money pattern to better represent and create monetary operations.
 *
 * @author edwin.njeru
 */
public interface Cash extends Comparable<Cash>, IsNumberical, HasDenomination {

    /**
     * @param arg {@link Cash} amount for comparison
     * @return True if the parameter argument is more than this amount
     */
    boolean isMoreThan(Cash arg);

    /**
     * @param arg {@link Cash} amount for comparison
     * @return True if the parameter argument is less than this amount
     */
    boolean isLessThan(Cash arg);

    /**
     * @param arg {@link Cash} amount to be added to this
     * @return New instance of {@link Cash} being the summation of this and the argument
     */
    Cash plus(Cash arg);

    /**
     * @param arg {@link Cash} amount to be subtracted to this
     * @return New instance of {@link Cash} being the remainder when the argument is subtracted from this
     */
    Cash minus(Cash arg);

    /**
     * Multiplies this using a double amount using {@link RoundingMode#HALF_EVEN}
     *
     * @param arg double amount by which we are to multiply this
     * @return New instance of {@link Cash} object containing multiplied amount
     */
    Cash multiply(double arg);

    /**
     * Multiplies this using a double amount and an explicit {@link RoundingMode}
     *
     * @param arg          double amount by which we are to multiply this
     * @param roundingMode {@link RoundingMode} to apply to the result
     * @return New instance of {@link Cash} object containing multiplied amount
     */
    Cash multiply(double arg, RoundingMode roundingMode);

    /**
     * Divides this by the double amount in the parameter, using {@link RoundingMode#HALF_EVEN}
     *
     * @param arg double amount by which we are to divide this
     * @return New instance of {@link Cash} object containing divided amount
     */
    Cash divide(double arg);

    /**
     * Divides this by the double amount in the parameter, using an explicit {@link RoundingMode}
     *
     * @param arg          double amount by which we are to divide this
     * @param roundingMode {@link RoundingMode} to apply to the result
     * @return New instance of {@link Cash} object containing divided amount
     */
    Cash divide(double arg, RoundingMode roundingMode);

    /**
     * @return True if the instrinsic amount in the {@link Cash} object is zero
     */
    boolean isZero();

    /**
     * @return {@link Cash} as absolute amount
     */
    Cash abs();
}
