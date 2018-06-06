/*
 * Copyright © 2018 Edwin Njeru (mailnjeru@gmail.com)
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
package io.github.ghacupha.keeper.book.api;

import io.github.ghacupha.cash.Cash;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.base.EntryDetails;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;

import java.util.Set;

/**
 * A collection of {@link Entry} items being posted at the same time
 */
public interface Transaction {

    /**
     *
     * @param accountSide {@link AccountSide} in which the entry is for
     * @param account {@link Account} into which the {@link Entry} is posted
     * @param details {@link EntryDetails} specifications of the {@link Entry}
     */
    void addEntry(AccountSide accountSide, Cash amount, Account account, EntryDetails details) throws ImmutableEntryException, MismatchedCurrencyException;

    /**
     * Adds the {@link Entry} items into the accounts involved in this {@link Transaction}
     */
    void post() throws UnableToPostException, ImmutableEntryException;

    /**
     *
     * @return Collection of {@link Entry} items in the Transaction
     */
    Set<Entry> getEntries();
}
