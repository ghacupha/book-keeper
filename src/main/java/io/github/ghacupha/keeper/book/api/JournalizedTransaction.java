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

package io.github.ghacupha.keeper.book.api;

import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;

import java.util.Currency;

public interface JournalizedTransaction {

    /**
     * This method creates a {@link Entry} and adds that to the {@link Account} in the parameter
     * and also adds it to this {@link Transaction} provided the {@link Transaction} is not already posted
     *
     * @param amount     {@link Cash} amount being posted to the journal
     * @param account    {@link Account} into which the {@link Entry} is being added
     * @param attributes {@link EntryAttributes} related to the Entry being added to the {@link Account} that is
     *                   being created in this method.
     * @throws ImmutableEntryException     Thrown if the {@link Transaction} is already posted when this method is running
     * @throws MismatchedCurrencyException Thrown if the {@link Currency} of the {@link Entry} or {@link Account}
     *                                     is mismatched with the {@link Currency} of this {@link Transaction}
     */
    void add(JournalSide journalSide,Cash amount, Account account, EntryAttributes attributes) throws ImmutableEntryException, MismatchedCurrencyException;

    /**
     * Posts the transactions into respective {@link Account} items
     *
     * @throws UnableToPostException {@link UnableToPostException} thrown when the transaction is not balanced
     *                               That is if the items posted on the debit are more than those posted on the credit or vice versa.
     */
    void post() throws UnableToPostException;
}
