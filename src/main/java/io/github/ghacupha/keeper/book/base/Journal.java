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
import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.DateRange;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;

/**
 * This is a container for {@link java.util.Collection} of entries with the ability to return
 * {@link AccountBalance} on request.
 *
 * @author edwin.njeru
 */
public class Journal implements Account {

    private static final Logger log = LoggerFactory.getLogger(Journal.class);

    protected volatile JournalSide journalSide;
    private final Currency currency;
    private final AccountAttributes accountAttributes;
    protected Collection<Entry> entries = new HashSet<>();


    public Journal(JournalSide journalSide, Currency currency, AccountAttributes accountAttributes) {
        this.journalSide = journalSide;
        this.currency = currency;
        this.accountAttributes = accountAttributes;

        log.debug("Journal created : {}", this);
    }

    private static Cash getCashAmount(Entry filteredEntry) {
        Cash amount = filteredEntry.getAmount();
        log.debug("Accounting entry : {} added into the balance with amount : {}", filteredEntry, amount);
        return amount;
    }

    @Override
    public void addEntry(Entry entry) {

        log.debug("Adding entry to account : {}", entry);

        try {
            assertCurrency(Currency.getInstance(entry.getAmount().getCurrency().getCurrencyCode()));
            assertBookingDate(entry.getBookingDate());


            entries.add(entry); // done

        } catch (MismatchedCurrencyException e) {
            log.error("Exception encountered when adding amount : {} to account : {}", entry.getAmount(), accountAttributes.getAccountName());
            e.printStackTrace();
        } catch (UntimelyBookingDateException e) {
            log.error("The booking date : {} is sooner than the account's opeing date : {}", entry.getBookingDate(), accountAttributes.getOpeningDate());
            e.printStackTrace();
        }

    }

    private void assertBookingDate(TimePoint bookingDate) throws UntimelyBookingDateException {

        // booking date cannot be sooner than account opening date
        if (bookingDate.before(accountAttributes.getOpeningDate())) {
            String message = String.format("The booking date cannot be earlier than the account opening date : Opening date : %s . " + "The entry date was %s", this.accountAttributes.getOpeningDate(), bookingDate);
            throw new UntimelyBookingDateException(message);
        }
    }

    private void assertCurrency(Currency currency) throws MismatchedCurrencyException {

        if (!this.currency.equals(currency)) {

            String message = String.format("The monetary amount added is inconsistent with this account :" + "Expected currency : %s but found %s", this.currency.toString(), currency.toString());
            throw new MismatchedCurrencyException(message);
        }
    }

    private AccountBalance balance(DateRange dateRange) {

        final Cash[] result = {new HardCash(0, currency)};

        entries.stream()
                .filter(entry -> dateRange.includes(entry.getBookingDate()))
                .map(Journal::getCashAmount)
                .forEachOrdered(orderedAmount -> {
                    log.debug("Adding amount : {}", orderedAmount);
                    result[0] = result[0].plus(orderedAmount);
                });

        log.debug("Returning balance of amount : {} on the {} side", result[0], journalSide);

        return new AccountBalance(result[0], journalSide);
    }

    @Override
    public AccountBalance balance(TimePoint asAt) {

        AccountBalance balance = balance(new DateRange(accountAttributes.getOpeningDate(), asAt));

        log.debug("Returning accounting balance as at : {} as : {}", asAt, balance);

        return balance;
    }

    @Override
    public AccountBalance balance() {
        return balance(new Moment());
    }

    @Override
    public String toString() {
        return this.accountAttributes.toString();
    }

    @Override
    public Currency getCurrency() {

        return currency;
    }

    @Override
    public JournalSide getJournalSide() {
        return journalSide;
    }

    protected AccountAttributes getAttributes(){

        return accountAttributes;
    }
}
