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
import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.DateRange;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.JournalSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.JournalSide.DEBIT;

/**
 * This implementation of the {@link Account} interface gives the balances for the entries as absolute
 * {@link AccountBalance} despite reversal in excess of {@link Cash} amount initially posted in the
 * direction of the side of the {@link Account}. When the sign changes the {@link JournalSide} of the
 * account is changed inorder to be more representative of the business domain.
 * The nature of this {@link Account} is such that it can belong to only one {@link Currency}
 *
 * @author edwin.njeru
 */
public class DirectedJournal extends JournalDecorator implements Account {

    private static final Logger log = LoggerFactory.getLogger(DirectedJournal.class);

    /**
     * Creates {@link Account} instance with tracking for {@link JournalSide} for {@link Entry}
     * items added into the account
     *
     * @param journalSide {@link JournalSide} to which the {@link Account} belongs. Indicates whether the {@link Account}
     *                                       is on the {@link JournalSide#CREDIT} side or {@link JournalSide#DEBIT}
     * @param currency {@link Currency} the {@link Account} is in. This {@link Account} can only have one {@link Currency}
     * @param accountAttributes {@link AccountAttributes} describing the designation and nature of the {@link Account}
     */
    public DirectedJournal(JournalSide journalSide, Currency currency, AccountAttributes accountAttributes) {
        super(journalSide, currency, accountAttributes);
    }

    @Override
    public AccountBalance balance(TimePoint asAt) {

        AccountBalance balance = balance(new DateRange(this.getAttributes().getOpeningDate(), asAt));
        log.debug("Returning accounting balance as at : {} as : {}", asAt, balance);

        return balance;
    }

    @Override
    public AccountBalance balance() {

        return balance(new Moment());
    }

    private AccountBalance balance(DateRange dateRange) {

        // What to do???
        final Cash amount = HardCash.of(0.00, getCurrency().getCurrencyCode());

        this.entries
                .stream()
                .filter(entry -> dateRange.includes(entry.getBookingDate()))
                .forEach(entry ->
                        calculateBalanceAmount(entry, amount)
                );

        return new AccountBalance(amount, this.journalSide);
    }

    private void calculateBalanceAmount(Entry entry, Cash amount) {

        if (this.journalSide == DEBIT) {

            if (entry.getJournalSide() == DEBIT) {
                amount = amount.plus(entry.getAmount());
                // maintain side
            }

            if (entry.getJournalSide() == CREDIT) {

                if (amount.isLessThan(entry.getAmount())) {

                    switchJournalSide(journalSide);

                    amount = amount.minus(entry.getAmount()).abs();

                }

                if (amount.isMoreThan(entry.getAmount())) {

                    // If you are here you don't need to change sides
                    amount = amount.minus(entry.getAmount()).abs();
                }
            }
        }

        if (this.journalSide == CREDIT) {

            if (entry.getJournalSide() == CREDIT) {

                // no problem. Add amount maintain balance, don't change side
                amount = amount.plus(entry.getAmount());
            }

            if (entry.getJournalSide() == DEBIT) {

                if (amount.isLessThan(entry.getAmount())) {

                    switchJournalSide(journalSide);

                    amount = amount.minus(entry.getAmount()).abs();

                }

                if (amount.isMoreThan(entry.getAmount())) {

                    // If you are here you don't need to change sides
                    amount = amount.minus(entry.getAmount()).abs();
                }
            }
        }
    }

    private void switchJournalSide(JournalSide journalSide) {

        if (journalSide == DEBIT) {

            journalSide = CREDIT;

        }

        if (journalSide == CREDIT) {

            journalSide = DEBIT;
        }

    }
}
