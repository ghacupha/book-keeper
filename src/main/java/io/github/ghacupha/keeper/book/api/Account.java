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

package io.github.ghacupha.keeper.book.api;

import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;

import java.util.Currency;

/**
 * Forms a collection of related {@link Entry}. The {@link org.joda.money.CurrencyUnit} of the
 * {@link Entry} must the same with that of the {@link Account}
 *
 * @author edwin.njeru
 */
public interface Account {

    /**
     * Adds a new entry to the account
     *
     * @param entry {@link Entry} to be added to the account
     */
    void addEntry(Entry entry) throws UntimelyBookingDateException, MismatchedCurrencyException;

    /**
     * Gives the account balance as at the {@link TimePoint} given
     *
     * @param asAt {@link TimePoint} effective which we require the account balance
     * @return {@link AccountBalance} effective at the {@link TimePoint} provided
     */
    AccountBalance balance(TimePoint asAt);

    /**
     * Gives the balance at the current {@link TimePoint} for this {@link Account}
     *
     * @return The {@link AccountBalance} as of now
     */
    AccountBalance balance();

    Currency getCurrency();

    TimePoint getOpeningDate();

    JournalSide getJournalSide();
}
