/*
 * Copyright 2018 Edwin Njeru
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

import io.github.ghacupha.keeper.book.api.*;
import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;

import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;

class FixedJournalTransaction extends AccountingTransactionDecorator implements Transaction,JournalizedTransaction {

    //TODO override add method in super and implement add method in JournalizedTransaction interface

    private boolean wasPosted;
    private final Currency currency;
    private final TimePoint date;
    private Collection<Entry> entries = new HashSet<>();


    public FixedJournalTransaction(TimePoint date, Currency currency) {
        super(date, currency);
        this.currency=currency;
        this.date=date;
    }

    @Override
    public void add(JournalSide journalSide, Cash amount, Account account, EntryAttributes attributes) throws ImmutableEntryException, MismatchedCurrencyException {

        if (wasPosted) {

            throw new ImmutableEntryException("Cannot add entry to a transaction that's already posted");
        }else if (!account.getCurrency().equals(currency)) {
            throw new MismatchedCurrencyException("Cannot add entry whose currency differs to that of the transaction");
        } else {
            entries.add(new JournalizedEntry(account,attributes,amount,date,this,journalSide));
        }
    }
}
