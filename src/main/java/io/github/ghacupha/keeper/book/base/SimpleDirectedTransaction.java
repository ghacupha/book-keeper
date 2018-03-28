/*
 *  Copyright 2018 Edwin Njeru
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

package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.api.DirectedTransaction;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;
import static java.util.function.Predicate.isEqual;

class SimpleDirectedTransaction extends AccountingTransactionDecorator implements DirectedTransaction {

    private static final Logger log = LoggerFactory.getLogger(SimpleDirectedTransaction.class);

    //TODO override add method in super and implement add method in DirectedTransaction interface

    private boolean wasPosted;
    private Collection<Entry> directionalEntries = new HashSet<>();

    public SimpleDirectedTransaction(TimePoint date, Currency currency) {
        super(new AccountingTransaction(date, currency));

        log.debug("SimpleDirectedTransaction created : {} with parameters date : {} and currency : {}", this, date, currency);
    }

    @Override
    public void add(AccountSide accountSide, Cash amount, Account account, EntryAttributes attributes) throws ImmutableEntryException, MismatchedCurrencyException {

        Entry journalEntry = new SimpleDirectedEntry(account, attributes, amount, getDate(), this, accountSide);

        log.debug("Adding entry : {} to the Transaction {}", journalEntry, this);
        if (wasPosted) {

            throw new ImmutableEntryException("Cannot add entry to a transaction that's already posted");

        } else if (!account.getCurrency().equals(getCurrency()) || !account.getCurrency().equals(amount.getCurrency())) {

            String message = String.format("Mismatched currencies: transaction currency : %s, Account Currency : %s, Cash currency : %s", getCurrency(), account.getCurrency(), amount.getCurrency());

            throw new MismatchedCurrencyException(message);

        } else {

            directionalEntries.add(journalEntry);
        }
    }

    private boolean currenciesAreMatched() {

        List<Entry> entryList = new ArrayList<>(directionalEntries);

        log.debug("Checking for currency mismatch in transaction : {}", this);

        Currency transactionCurrency = entryList.get(0).getAmount().getCurrency();

        boolean matched = entryList.stream().map(entry -> entry.getAmount().getCurrency()).allMatch(isEqual(transactionCurrency));
        log.debug("Currencies matched : {}", matched);

        return matched;
    }

    /**
     * Posts the transactions into respective {@link Account} items
     *
     * @throws UnableToPostException {@link UnableToPostException} thrown when the transaction is not balanced
     *                               That is if the items posted on the debit are more than those posted on the credit or vice versa.
     */
    @Override
    public void post() throws UnableToPostException, MismatchedCurrencyException {

        if (currenciesAreMatched()) {

            if (!(imbalance() == 0.00)) {

                double diff = imbalance();
                if (diff > 0.00) {
                    String message = String.format("Unable to post unbalanced transaction entries. Debits are more than credits by %s", diff);
                    throw new UnableToPostException(message);
                }
                if (diff < 0.00) {
                    String message = String.format("Unable to post unbalanced transaction entries. Credits are more than debits by %s", diff * -1);
                    throw new UnableToPostException(message);
                }

            } else {

                directionalEntries.forEach(Entry::post);

                wasPosted = true;
            }
        } else {

            throw new MismatchedCurrencyException(String.format("Can't post with mismatched entry-currencies in transaction : %s", this));
        }
    }


    private double imbalance() {
        return balanced(this.getEntries());
    }

    private double balanced(Collection<Entry> directionalEntries) {

        log.debug("Checking if the transaction is balanced");

        double debitEntries = directionalEntries.stream().filter(entry -> entry.getAccountSide() == DEBIT).map(entry -> entry.getAmount().getNumber().doubleValue()).reduce(0.00, (x, y) -> x + y);

        double creditEntries = directionalEntries.stream().filter(entry -> entry.getAccountSide() == CREDIT).map(entry -> entry.getAmount().getNumber().doubleValue()).reduce(0.00, (x, y) -> x + y);

        log.debug("Transaction contains debits amounting to : {} and credits amount to : {}", debitEntries, creditEntries);

        return debitEntries - creditEntries;
    }

    /**
     * @return Unmodifiable list of {@link Entry} items from this
     */
    public List<Entry> getEntries() {

        return Collections.unmodifiableList(new ArrayList<>(directionalEntries));
    }
}
