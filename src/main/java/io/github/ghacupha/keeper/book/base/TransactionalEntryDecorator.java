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
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.api.Transaction;
import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;

/**
 * Was created in order to add functionality to the {@link TransactionalEntry} class
 * in a way that would make it possible to an {@link Entry} to have a pointer that
 * denotes the {@link JournalSide} to which it belongs
 *
 * @author edwin.njeru
 */
class TransactionalEntryDecorator extends TransactionalEntry implements Entry {

    TransactionalEntryDecorator(Account forJournal, EntryAttributes entryAttributes, Cash amount, TimePoint bookingDate, Transaction transaction) {
        super(forJournal, entryAttributes, amount, bookingDate, transaction);
    }
}
