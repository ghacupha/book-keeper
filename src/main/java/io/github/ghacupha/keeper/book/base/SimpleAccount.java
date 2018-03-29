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
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.DateRange;
import io.github.ghacupha.keeper.book.unit.time.SimpleDate;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;

public final class SimpleAccount implements Account {

    private static final Logger log = LoggerFactory.getLogger(SimpleAccount.class);

    private final Currency currency;
    private final AccountDetails accountDetails;
    private volatile AccountSide accountSide;

    private final List<Entry> entries = new CopyOnWriteArrayList<>();

    public SimpleAccount(AccountSide accountSide,Currency currency,  AccountDetails accountDetails) {
        this.currency = currency;
        this.accountSide = accountSide;
        this.accountDetails = accountDetails;
    }

    /**
     * @param entry {@link Entry} to be added to this
     */
    @Override
    public void addEntry(Entry entry) throws MismatchedCurrencyException, UntimelyBookingDateException {

        log.debug("Adding entry to account : {}", entry);

        if (entry.getBookingDate().before(accountDetails.getOpeningDate())) {

            String message = String.format("Opening date : %s . The entry date was %s", this.accountDetails.getOpeningDate(), entry.getBookingDate());
            throw new UntimelyBookingDateException("The booking date cannot be earlier than the account opening date : " + message);

        } else if (!this.currency.equals(entry.getAmount().getCurrency())) {

            String message = String.format("Currencies mismatched :Expected getCurrency : %s but found entry denominated in %s", this.currency.toString(), entry.getAmount().getCurrency());
            throw new MismatchedCurrencyException(message);

        } else {

            entries.add(entry); // done
        }
    }

    /**
     * Returns the balance of the Account
     *
     * @param asAt {@link TimePoint} at which is Effective
     * @return {@link AccountBalance}
     */
    @Override
    public AccountBalance balance(TimePoint asAt) {

        log.debug("Account balance enquiry raised as at {}", asAt);

        AccountBalance balance = balance(new DateRange(accountDetails.getOpeningDate(), asAt));

        log.debug("Returning accounting balance as at : {} as : {}", asAt, balance);

        return balance;
    }

    /**
     * Similar to the balance query for a given date except the date is provided through a
     * simple varags int argument
     *
     * @param asAt The date as at when the {@link AccountBalance} we want is effective given
     *             in the following order
     *             i) Year
     *             ii) Month
     *             iii) Date
     * @return {@link AccountBalance} effective the date specified by the varargs
     */
    @Override
    public AccountBalance balance(int... asAt) {
        AccountBalance balance = balance(new SimpleDate(asAt[0],asAt[1],asAt[2]));

        log.debug("Returning accounting balance as at : {} as : {}", asAt, balance);

        return balance;
    }

    private AccountBalance balance(DateRange dateRange){

        Cash debits = getDebits(dateRange);

        Cash credits = getCredits(dateRange);

        if (debits.isZero() || credits.isZero()) {
            if(!debits.isZero() && credits.isZero()){
                return new AccountBalance(debits, DEBIT);
            } else if(debits.isZero() && !credits.isZero()){
                return new AccountBalance(credits, CREDIT);
            }
        } else if (this.accountSide == DEBIT) {

            if (credits.isMoreThan(debits)) {
                return new AccountBalance(credits.minus(debits).abs(), CREDIT);
            }

            if (!credits.isMoreThan(debits)) {
                return new AccountBalance(credits.minus(debits).abs(), DEBIT);
            }
        } else if (this.accountSide == CREDIT) {

            if (debits.isMoreThan(credits)) {
                return new AccountBalance(debits.minus(credits).abs(), DEBIT);
            }

            if (!debits.isMoreThan(credits)) {
                return new AccountBalance(debits.minus(credits).abs(), CREDIT);
            }
        }

        return new AccountBalance(HardCash.of(0.0,this.currency),this.accountSide);
    }

    private Cash getCredits(DateRange dateRange) {
        return HardCash.of(this.getEntries()
            .parallelStream()
            .filter(entry -> dateRange.includes(entry.getBookingDate()))
            .filter(entry -> entry.getAccountSide() == CREDIT)
            .map(entry -> entry.getAmount().getNumber().doubleValue())
            .reduce(0.00,(acc,value) -> acc + value), this.getCurrency());
    }

    private Cash getDebits(DateRange dateRange) {
        return HardCash.of(this.getEntries()
            .parallelStream()
            .filter(entry -> dateRange.includes(entry.getBookingDate()))
            .filter(entry -> entry.getAccountSide() == DEBIT)
            .map(entry -> entry.getAmount().getNumber().doubleValue())
            .reduce(0.00,(acc,value) -> acc + value), this.getCurrency());
    }

    private static boolean entryIsCreditingUs(Entry entry) {
        return entry.getAccountSide() == CREDIT;
    }

    /**
     * @return Currency of the account
     */
    @Override
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @return {@link AccountSide} position of {@link Account} in the
     * balance sheet
     */
    @Override
    public AccountSide accountSide() {
        return accountSide;
    }

    @Override
    public TimePoint openingDate() {

        return accountDetails.getOpeningDate();
    }

    private List<Entry> getEntries() {

        return entries.stream().collect(ImmutableListCollector.toImmutableList());
    }

    @Override
    public TimePoint getOpeningDate() {
        return this.accountDetails.getOpeningDate();
    }

    @Override
    public String toString() {
        return this.accountDetails.getNumber()+" "+this.accountDetails.getName();
    }
}
