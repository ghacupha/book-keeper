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

import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.api.Transaction;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;

/**
 * This object is for use with {@link AccountingTransaction} which allows us to maintain a
 * 2-way relation between the relationship and the account.
 * The {@link Entry} objects will be immutable making the two-way link easier to maintain
 *
 * @author edwin.njeru
 */
public class TransactionalEntry extends AccoutingEntryDecorator implements Entry {

    private Transaction transaction;

    public TransactionalEntry(io.github.ghacupha.keeper.book.base.Account forAccount, EntryAttributes entryAttributes, Cash amount, TimePoint bookingDate, Transaction transaction) {
        super(forAccount, entryAttributes, amount, bookingDate);
        this.transaction = transaction;
    }
}
