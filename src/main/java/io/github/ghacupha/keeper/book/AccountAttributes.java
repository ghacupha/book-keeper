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

package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;

public interface AccountAttributes {
    /**
     *
     * @return Name of the {@link Account} as String
     */
    String getAccountName();

    /**
     *
     * @return Unique number designated for the {@link Account} as String
     */
    String getAccountNumber();

    /**
     *
     * @return The date the account was opened as {@link TimePoint}
     */
    TimePoint getOpeningDate();

    /**
     * Returns the string attribute required in the parameter
     * @param attribute String designation for the attribute required
     * @return {@link Account} attribute value returned as String
     */
    String getStringAttribute(String attribute);

    /**
     * Returns the {@link Cash} attribute required in the parameter
     * @param attribute String designation for the attribute required
     * @return {@link Account} attribute value returned as {@link Cash}
     */
    Cash getCashAttribute(String attribute);

    /**
     * Returns the {@link TimePoint} attribute required in the parameter
     * @param attribute String designation for the attribute required
     * @return {@link Account} attribute value returned as {@link TimePoint}
     */
    TimePoint getTimePointAttribute(String attribute);

    /**
     * Returns the {@link Object} attribute required in the parameter
     * @param attribute String designation for the attribute required
     * @return {@link Account} attribute value returned as {@link Object}
     */
    Object getObjectAttribute(String attribute);

    /**
     * Sets the string attribute as specified in the argument
     * @param attribute The designation of the attribute
     * @param arg The string value of the attribute
     */
    void setStringAttribute(String attribute, String arg);

    /**
     * Sets the {@link Cash} attribute as specified in the argument
     * @param attribute The designation of the attribute
     * @param arg The {@link Cash} value of the attribute
     */
    void setCashAttribute(String attribute, Cash arg);

    /**
     * Sets the {@link TimePoint} attribute as specified in the argument
     * @param attribute The designation of the attribute
     * @param arg The {@link TimePoint} value of the attribute
     */
    void setTimePointAttribute(String attribute, TimePoint arg);

    /**
     * Sets the {@link Object} attribute as specified in the argument
     * @param attribute The designation of the attribute
     * @param arg The {@link Object} value of the attribute
     */
    void setObjectAttribute(String attribute, Object arg);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
