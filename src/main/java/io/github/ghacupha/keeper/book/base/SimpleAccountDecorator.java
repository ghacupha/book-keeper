/*
 *  Copyright 2018 Edwin Njeru
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
import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;

/**
 * This Decorator implements the {@link Account} by wrapping its {@link SimpleAccount}
 * implementation
 *
 * @author edwin.njeru
 */
public class SimpleAccountDecorator implements Account {

    private final SimpleAccount simpleAccount;

    public SimpleAccountDecorator(AccountSide accountSide, Currency currency, AccountAttributes accountAttributes) {
        simpleAccount = new SimpleAccount(accountSide, currency, accountAttributes);
    }

    @Override
    public void addEntry(Entry entry) {
        try {
            this.simpleAccount.addEntry(entry);
        } catch (UntimelyBookingDateException | MismatchedCurrencyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AccountBalance balance(TimePoint asAt) {
        return simpleAccount.balance(asAt);
    }

    @Override
    public AccountBalance balance() {
        return simpleAccount.balance();
    }

    @Override
    public Currency getCurrency() {
        return simpleAccount.getCurrency();
    }

    @Override
    public TimePoint getOpeningDate() {

        return simpleAccount.getOpeningDate();
    }

    @Override
    public AccountSide getAccountSide() {
        return simpleAccount.getAccountSide();
    }

    @Override
    public String toString() {
        return simpleAccount.toString();
    }


    AccountAttributes getAttributes() {

        return simpleAccount.getAttributes();
    }

    Collection<Entry> getEntries() {

        return Collections.unmodifiableList(new ArrayList<>(simpleAccount.getEntries()));
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

        return simpleAccount.balance(asAt);
    }
}
