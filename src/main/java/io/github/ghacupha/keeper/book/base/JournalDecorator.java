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
import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;

/**
 * This Decorator implements the {@link Account} by wrapping its {@link Journal}
 * implementation
 *
 * @author edwin.njeru
 */
public class JournalDecorator implements Account {

    private final Journal journal;

    public JournalDecorator(JournalSide journalSide, Currency currency, AccountAttributes accountAttributes) {
        journal = new Journal(journalSide, currency, accountAttributes);
    }

    @Override
    public void addEntry(Entry entry) {
        try {
            this.journal.addEntry(entry);
        } catch (UntimelyBookingDateException e) {
            e.printStackTrace();
        } catch (MismatchedCurrencyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AccountBalance balance(TimePoint asAt) {
        return journal.balance(asAt);
    }

    @Override
    public AccountBalance balance() {
        return journal.balance();
    }

    @Override
    public Currency getCurrency() {
        return journal.getCurrency();
    }

    @Override
    public TimePoint getOpeningDate() {

        return journal.getOpeningDate();
    }

    @Override
    public JournalSide getJournalSide() {
        return journal.getJournalSide();
    }

    @Override
    public String toString() {
        return journal.toString();
    }


    AccountAttributes getAttributes() {

        return journal.getAttributes();
    }

    Collection<Entry> getEntries() {

        return Collections.unmodifiableList(new ArrayList<>(journal.getEntries()));
    }
}
