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

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;

/**
 * This acts as a decorator to the {@link AccountingEntry} object inorder to give
 * additional functionality to the sub classes
 *
 * @author edwin.njeru
 */
class AccoutingEntryDecorator implements Entry {

    private final AccountingEntry accountingEntry;

    AccoutingEntryDecorator(Account account, EntryAttributes entryAttributes, Cash amount, TimePoint bookingDate) {
        accountingEntry = new AccountingEntry(account, entryAttributes, amount, bookingDate);
    }

    @Override
    public Entry newEntry(Account journal, EntryAttributes entryAttributes, Cash amount, TimePoint bookingDate) {

        return accountingEntry.newEntry(journal, entryAttributes, amount, bookingDate);
    }

    @Override
    public EntryAttributes getEntryAttributes() {
        return accountingEntry.getEntryAttributes();
    }

    @Override
    public void setEntryAttributes(EntryAttributes entryAttributes) throws ImmutableEntryException {

        this.accountingEntry.setEntryAttributes(entryAttributes);
    }

    @Override
    public Cash getAmount() {
        return accountingEntry.getAmount();
    }

    @Override
    public TimePoint getBookingDate() {
        return accountingEntry.getBookingDate();
    }

    @Override
    public void post() {

        this.accountingEntry.post();
    }

    @Override
    public String toString() {
        return accountingEntry.toString();
    }
}
