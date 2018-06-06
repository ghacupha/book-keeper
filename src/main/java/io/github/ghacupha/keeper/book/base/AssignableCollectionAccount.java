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
package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;
import io.github.ghacupha.time.point.TimePoint;

import java.util.Currency;
import java.util.List;

/**
 * In some really deadly way the constructor for this object looks like the {@link SimpleAccount} constructor,
 * with one really important difference, {@code List} could be assigned in the constructor
 */
@SuppressWarnings("unused")
public class AssignableCollectionAccount implements Account {

    private AccountAppraisalDelegate evalulationDelegate = new AccountAppraisalDelegate(this);

    private final Currency currency;
    private final AccountDetails accountDetails;
    private volatile AccountSide accountSide;

    private final List<Entry> entries;

    AssignableCollectionAccount(AccountSide accountSide,Currency currency,  AccountDetails accountDetails,List<Entry> entries) {
        this.currency = currency;
        this.accountSide = accountSide;
        this.accountDetails = accountDetails;
        this.entries=entries;
    }

    /**
     * @param entry {@link Entry} to be added to this
     */
    @Override
    public void addEntry(Entry entry) throws MismatchedCurrencyException, UntimelyBookingDateException {

    }

    /**
     * Returns the balance of the Account
     *
     * @param asAt {@link TimePoint} at which is Effective
     * @return {@link AccountBalance}
     */
    @Override
    public AccountBalance balance(TimePoint asAt) {
        return null;
    }

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
    @Override
    public AccountBalance balance(int... asAt) {
        return null;
    }

    /**
     * @return Currency of the account
     */
    @Override
    public Currency getCurrency() {
        return null;
    }

    /**
     * @return {@link TimePoint} date when the account was opened
     */
    @Override
    public TimePoint getOpeningDate() {
        return null;
    }

    /**
     * @return Shows the side of the balance sheet to which this belongs which could be either
     * {@link AccountSide#DEBIT} or {@link AccountSide#CREDIT}
     * As per implementation notes this is for use only by the {@link AccountAppraisalDelegate}
     * allowing inexpensive evaluation of the {@link AccountBalance} without causing circular reference. Otherwise anyone else who needs
     * to know the {@code AccountSide} of this needs to query the {@link AccountBalance} first, and from it acquire the {@link AccountSide}
     */
    @Override
    public AccountSide getAccountSide() {
        return null;
    }

    /**
     * @return Returns this object's current copy of the {@link Entry} items
     */
    @Override
    public List<Entry> getEntries() {
        return null;
    }

    @Override
    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    @Override
    public void setAccountSide(final AccountSide accountSide) {

        this.accountSide = accountSide;
    }
}
