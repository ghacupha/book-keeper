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

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.api.Transaction;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HasDenomination;
import io.github.ghacupha.keeper.book.unit.time.IsChronological;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;

import java.util.Currency;

class AccountingTransactionDecorator implements Transaction,HasDenomination,IsChronological {

    private final AccountingTransaction accountingTransaction;

    public AccountingTransactionDecorator(AccountingTransaction accountingTransaction) {
        this.accountingTransaction = accountingTransaction;
    }

    @Override
    public void add(Cash amount, Account account, EntryAttributes attributes) throws ImmutableEntryException, MismatchedCurrencyException {
        accountingTransaction.add(amount,account,attributes);
    }

    @Override
    public void post() throws UnableToPostException, MismatchedCurrencyException {

        accountingTransaction.post();
    }

    @Override
    public Currency getCurrency() {

        return accountingTransaction.getCurrency();
    }

    @Override
    public TimePoint getDate() {

        return accountingTransaction.getDate();
    }
}
