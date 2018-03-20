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

package io.github.ghacupha.keeper.book.internal;

import io.github.ghacupha.keeper.book.Account;
import io.github.ghacupha.keeper.book.Entry;
import io.github.ghacupha.keeper.book.EntryAttributes;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;

/**
 * This acts as a decorator to the {@link AccountingEntry} object inorder to give
 * additional functionality to the sub classes
 *
 * @author edwin.njeru
 */
public class AccoutingEntryDecorator extends AccountingEntry implements Entry {

    public AccoutingEntryDecorator(Account forAccount, EntryAttributes entryAttributes, Cash amount, TimePoint bookingDate) {
        super(forAccount, entryAttributes, amount, bookingDate);
    }
}
