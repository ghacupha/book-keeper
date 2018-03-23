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
import io.github.ghacupha.keeper.book.api.Transaction;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;

import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Encapsulates a group of related {@link Entry} objects which are posted to and from
 * various {@link Account} objects
 * This implementation is such that a transaction could contain several accounts posting
 * to a given account.
 * {@link Entry} items are added to this object through various method calls, and once all the transactions
 * are added, we check if they add up to Zero before we can post.
 * Once posted no more {@link Entry} items can be added
 *
 * @author edwin.njeru
 */
public class AccountingTransaction implements Transaction {

    // All entries will share a date
    private final TimePoint date;
    // All entries have the same currency
    private final Currency currency;
    private boolean wasPosted = false;
    private Collection<Entry> entries = new HashSet<>();

    /**
     * Creates a {@link Transaction} with an Immutable {@link TimePoint} transaction date
     * and {@link Currency}.
     *
     * @param date     {@link TimePoint} {@link Transaction}date assumed uniform for all {@link Entry} items
     * @param currency {@link Currency} of the {@link Transaction} also assumed uniform for all {@link Entry} items
     */
    public AccountingTransaction(TimePoint date, Currency currency) {
        this.date = date;
        this.currency = currency;
    }

    /**
     * The add method adds entries to the transaction provided the transaction has not already
     * been posted
     *
     * @param amount  {@link Cash} amount being posted to the journal
     * @param account {@link Account} into which the {@link Entry} is being added
     */
    @Override
    public void add(Cash amount, Account account, EntryAttributes attributes) throws ImmutableEntryException, MismatchedCurrencyException {

        if (wasPosted) {
            throw new ImmutableEntryException("Cannot add entry to a transaction that's already posted");
        } else if (!account.getCurrency().equals(this.currency)) {
            throw new MismatchedCurrencyException("Cannot add entry whose currency differs to that of the transaction");
        } else {
            entries.add(new TransactionalEntry(account, attributes, amount, date, this));
        }
    }

    /**
     * Posts the transactions into respective {@link Account} items
     *
     * @throws UnableToPostException {@link UnableToPostException} thrown when the transaction is not balanced
     *                               That is if the items posted on the debit are more than those posted on the credit or vice versa.
     */
    @Override
    public void post() throws UnableToPostException {

        if (!canPost()) {

            throw new UnableToPostException();
        } else {

            entries.forEach(Entry::post);

            wasPosted = true;
        }
    }

    protected boolean canPost() {

        return balance().isZero();
    }

    private Cash balance() {
        if (entries.isEmpty()) {
            return HardCash.of(0.00, currency.getCurrencyCode());
        }

        Iterator it = entries.iterator();
        Entry firstEntry = (Entry) it.next();

        Cash result = firstEntry.getAmount();

        while (it.hasNext()) {
            Entry each = (Entry) it.next();
            result = result.plus(each.getAmount());
        }

        return result;
    }
}
