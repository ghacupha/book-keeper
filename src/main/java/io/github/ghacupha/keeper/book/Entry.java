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

package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.internal.AccountImpl;
import io.github.ghacupha.keeper.book.internal.EntryDetails;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;

/**
 * Collection of this {@link Entry} objects forms the {@link AccountImpl}, which is one of the
 * descriptors for an account
 * The {@link EntryDetails} is used as a descriptor, that could contain stuff like details, categories,
 * parties involved, invoice numbers etc
 *
 * @author edwin.njeru
 */
public interface Entry {

    Entry newEntry(AccountImpl account, EntryAttributes entryAttributes, HardCash amount, TimePoint bookingDate);

    EntryAttributes getEntryAttributes();

    void setEntryAttributes(EntryAttributes entryAttributes) throws ImmutableEntryException;

    Cash getAmount();

    TimePoint getBookingDate();

    /**
     * Posts the transactions into respective {@link Account} item.
     * This method is only to be used by a {@link Transaction} object
     *
     * @throws UnableToPostException {@link UnableToPostException} thrown when the related {@link Transaction} is not balanced
     *                               That is if the items posted on the debit are more than those posted on the credit or vice versa.
     */
    void post();
}
