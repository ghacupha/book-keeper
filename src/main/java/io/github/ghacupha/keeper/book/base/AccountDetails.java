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

package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Encapsulates information concerning the nature of an {@link SimpleAccount} to which
 * it belongs
 *
 * @author edwin.njeru
 */
public class AccountDetails implements AccountAttributes {

    private final String accountName;
    private final String accountNumber;
    private final TimePoint openingDate;

    //TODO getters to review availability of data from the maps
    private final Map<String, String> stringMap = new ConcurrentSkipListMap<>();
    private final Map<String, TimePoint> timePointMap = new ConcurrentSkipListMap<>();
    private final Map<String, Cash> cashMap = new ConcurrentSkipListMap<>();
    private final Map<String, Object> objectMap = new ConcurrentSkipListMap<>();

    public AccountDetails(String accountName, String accountNumber, TimePoint openingDate) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.openingDate = openingDate;
    }

    public static AccountAttributes newDetails(String accountName, String accountNumber, TimePoint openingDate) {

        return new AccountDetails(accountName, accountNumber, openingDate);
    }

    /**
     * @return Name of the {@link io.github.ghacupha.keeper.book.api.Account} as String
     */
    @Override
    public String getAccountName() {
        return accountName;
    }

    /**
     * @return Unique number designated for the {@link io.github.ghacupha.keeper.book.api.Account} as String
     */
    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @return The date the account was opened as {@link TimePoint}
     */
    @Override
    public TimePoint getOpeningDate() {
        return openingDate;
    }

    /**
     * Returns the string attribute required in the parameter
     *
     * @param attribute String designation for the attribute required
     * @return {@link io.github.ghacupha.keeper.book.api.Account} attribute value returned as String
     */
    @Override
    public String getStringAttribute(String attribute) {

        return stringMap.get(attribute);
    }

    /**
     * Returns the {@link Cash} attribute required in the parameter
     *
     * @param attribute String designation for the attribute required
     * @return {@link io.github.ghacupha.keeper.book.api.Account} attribute value returned as {@link Cash}
     */
    @Override
    public Cash getCashAttribute(String attribute) {

        return cashMap.get(attribute);
    }

    /**
     * Returns the {@link TimePoint} attribute required in the parameter
     *
     * @param attribute String designation for the attribute required
     * @return {@link io.github.ghacupha.keeper.book.api.Account} attribute value returned as {@link TimePoint}
     */
    @Override
    public TimePoint getTimePointAttribute(String attribute) {

        return timePointMap.get(attribute);
    }

    /**
     * Returns the {@link Object} attribute required in the parameter
     *
     * @param attribute String designation for the attribute required
     * @return {@link io.github.ghacupha.keeper.book.api.Account} attribute value returned as {@link Object}
     */
    @Override
    public Object getObjectAttribute(String attribute) {

        return objectMap.get(attribute);
    }

    /**
     * Sets the string attribute as specified in the argument
     *
     * @param attribute The designation of the attribute
     * @param arg       The string value of the attribute
     */
    @Override
    public void setStringAttribute(String attribute, String arg) {

        stringMap.put(attribute, arg);
    }

    /**
     * Sets the {@link Cash} attribute as specified in the argument
     *
     * @param attribute The designation of the attribute
     * @param arg       The {@link Cash} value of the attribute
     */
    @Override
    public void setCashAttribute(String attribute, Cash arg) {

        cashMap.put(attribute, arg);
    }

    /**
     * Sets the {@link TimePoint} attribute as specified in the argument
     *
     * @param attribute The designation of the attribute
     * @param arg       The {@link TimePoint} value of the attribute
     */
    @Override
    public void setTimePointAttribute(String attribute, TimePoint arg) {

        timePointMap.put(attribute, arg);
    }

    /**
     * Sets the {@link Object} attribute as specified in the argument
     *
     * @param attribute The designation of the attribute
     * @param arg       The {@link Object} value of the attribute
     */
    @Override
    public void setObjectAttribute(String attribute, Object arg) {

        objectMap.put(attribute, arg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountDetails that = (AccountDetails) o;

        if (!accountName.equals(that.accountName)) {
            return false;
        }
        if (!accountNumber.equals(that.accountNumber)) {
            return false;
        }
        if (!openingDate.equals(that.openingDate)) {
            return false;
        }
        if (!stringMap.equals(that.stringMap)) {
            return false;
        }
        if (!timePointMap.equals(that.timePointMap)) {
            return false;
        }
        if (!cashMap.equals(that.cashMap)) {
            return false;
        }
        return objectMap.equals(that.objectMap);
    }

    @Override
    public int hashCode() {
        int result = accountName.hashCode();
        result = 31 * result + accountNumber.hashCode();
        result = 31 * result + openingDate.hashCode();
        result = 31 * result + stringMap.hashCode();
        result = 31 * result + timePointMap.hashCode();
        result = 31 * result + cashMap.hashCode();
        result = 31 * result + objectMap.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountDetails{");
        sb.append("accountName='").append(accountName).append('\'');
        sb.append(", accountNumber='").append(accountNumber).append('\'');
        sb.append(", openingDate=").append(openingDate);
        sb.append('}');
        return sb.toString();
    }
}
