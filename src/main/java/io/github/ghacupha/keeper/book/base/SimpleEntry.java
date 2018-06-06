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
package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.cash.Cash;
import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;
import io.github.ghacupha.time.point.TimePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;

/**
 * <p>This is a thread-safe implementation of the {@link Entry} interface in which all internal states are final upon
 * initialization.</p>
 * <p>It contains the following fields:</p>
 * <p>1. {@link Account} representing the account to which it belongs.</p>
 * <p>2. {@link Cash} amount represented by this accounting {@link Entry}</p>
 * <p>3. {@link AccountSide} which shows whether the account is a {@link AccountSide#DEBIT} or {@link AccountSide#CREDIT}</p>
 * <p>4. {@link TimePoint} field showing the date on which this {@code Entry} was booked</p>
 * <p>5. {@link EntryDetails} field is a simple representation of the particulars of this {@code Entry}, the minimum of
 * which is a "narration" of what the {@code Entry} entails.</p>.
 *
 * Needless to say since this object contains an immutable Cash amount, the {@link Currency} of whatever money is contained
 * in this entry is consequently immutable,as is the {@link AccountSide}.
 *
 * @author edwin.njeru
 */
public final class SimpleEntry implements Entry {

    private static final Logger log = LoggerFactory.getLogger(SimpleEntry.class);

    // pointer to the Account
    private final Account forAccount;

    // how much the entry will contain, money wise
    private final Cash amount;

    // AccountSide
    private final AccountSide accountSide;

    // Date when the entry is booked
    private final TimePoint bookingDate;

    // description of the Entry
    private final EntryDetails entryDetails;

    SimpleEntry(AccountSide accountSide, Account forAccount, Cash amount, TimePoint bookingDate, EntryDetails entryDetails) {
        this.forAccount = forAccount;
        this.accountSide = accountSide;
        this.amount = amount;
        this.bookingDate = bookingDate;
        this.entryDetails = entryDetails;
    }

    @Override
    public EntryDetails getEntryDetails() {
        return entryDetails;
    }

    @Override
    public Currency getCurrency() {
        return amount.getCurrency();
    }

    @Override
    public AccountSide getAccountSide() {
        return accountSide;
    }

    @Override
    public TimePoint getBookingDate() {
        return bookingDate;
    }

    @Override
    public Cash getAmount() {
        return amount;
    }

    @Override
    public void post() {
        try {
            log.debug("Adding entry : {} into account : {}",this,forAccount);
            forAccount.addEntry(this);
        } catch (UntimelyBookingDateException e) {
            log.error("Could not post the entry : {} into forAccount : {}", this, forAccount, e.getStackTrace());

            log.error("Cause : the Entry booking date :{} is sooner than the forAccount's opening date {} ", bookingDate, forAccount.getOpeningDate(), e.getStackTrace());
        } catch (MismatchedCurrencyException e) {
            log.error("Could not post the entry : {} into the forAccount : {} ", this, forAccount, e.getStackTrace());
            log.error("Cause the entry's currency : {} does not match the forAccount's currency : {}", amount.getCurrency(), forAccount.getCurrency(), e.getStackTrace());
        }
    }

    @Override
    public String toString() {
        return entryDetails.toString();
    }
}
