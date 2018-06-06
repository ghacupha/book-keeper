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
package io.github.ghacupha.keeper.book.api;

import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.base.AccountAppraisalDelegate;
import io.github.ghacupha.keeper.book.base.AccountDetails;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;

import java.util.Currency;
import java.util.List;

/**
 * A collection of {@link Entry} items.
 * @implNote The specification of this interface hides the fact that the {@link Account} contains an {@link AccountSide}
 * field. This reduces excessive of the implementation internals, but if the implementation does not contain
 * an {@link AccountSide} then the point of this interface is moot. Therefore the only to safely access the
 * {@link AccountSide} is through the {@link AccountBalance}.
 *
 * @author edwin.njeru
 */
public interface Account {

    /**
     *
     * @param entry {@link Entry} to be added to this
     */
    void addEntry(Entry entry) throws MismatchedCurrencyException, UntimelyBookingDateException;

    /**
     * Returns the balance of the Account
     * @param asAt {@link TimePoint} at which is Effective
     * @return {@link AccountBalance}
     */
    AccountBalance balance(TimePoint asAt);

    /**
     * Similar to the balance query for a given date except the date is provided through a
     * simple varags int argument
     *
     * @param asAt The date as at when the {@link AccountBalance} we want is effective given
     *             in the following order
     *             i) Year
     *             ii) Month
     *             iii) Date
     * @return {@link AccountBalance} effective the date specified by the varargs
     */
    AccountBalance balance(int... asAt);

    /**
     *
     * @return Currency of the account
     */
    Currency getCurrency();

    /**
     *
     * @return {@link TimePoint} date when the account was opened
     */
    TimePoint getOpeningDate();

    /**
     * @implSpec As per implementation notes this is for use only by the {@link AccountAppraisalDelegate}
     * allowing inexpensive evaluation of the {@link AccountBalance} without causing circular reference. Otherwise anyone else who needs
     * to know the {@code AccountSide} of this needs to query the {@link AccountBalance} first, and from it acquire the {@link AccountSide}
     *
     * @return Shows the side of the balance sheet to which this belongs which could be either
     * {@link AccountSide#DEBIT} or {@link AccountSide#CREDIT}
     */
    AccountSide getAccountSide();

    /**
     *
     * @return Returns this object's current copy of the {@link Entry} items
     */
    List<Entry> getEntries();

    AccountDetails getAccountDetails();

    void setAccountSide(AccountSide accountSide);
}
