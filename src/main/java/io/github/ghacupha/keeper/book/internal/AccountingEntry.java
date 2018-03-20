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
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements {@link Entry} interface and represents the base item in a transaction.
 * Once it is closed setting {@link EntryAttributes} will throw a {@link ImmutableEntryException}
 *
 * @author edwin.njeru
 */
public class AccountingEntry implements Entry {

    private static final Logger log = LoggerFactory.getLogger(AccountingEntry.class);

    private EntryAttributes entryAttributes;
    private boolean open;
    private TimePoint bookingDate;
    private Account forAccount;
    private Cash amount;

    public AccountingEntry(Account forAccount, EntryAttributes entryAttributes, Cash amount, TimePoint bookingDate) {
        this.bookingDate = bookingDate;
        this.open = true;
        this.amount = amount;
        try {
            this.setEntryAttributes(entryAttributes);
        } catch (ImmutableEntryException e) {
            e.printStackTrace();
        }
        this.forAccount = forAccount;
        this.bookingDate = bookingDate;

        log.debug("Accounting entry created : {}", this);
    }

    @Override
    public Entry newEntry(AccountImpl account, EntryAttributes entryAttributes, HardCash amount, TimePoint bookingDate) {

        return new AccountingEntry(account, entryAttributes, amount, bookingDate);

    }

    @Override
    public EntryAttributes getEntryAttributes() {
        log.debug("Returning entry details : {}", entryAttributes);

        return entryAttributes;
    }

    @Override
    public void setEntryAttributes(EntryAttributes entryAttributes) throws ImmutableEntryException {

        log.debug("Setting EntryDetails : {}", entryAttributes);
        if (isOpen()) {

            log.debug("Entry is open. Setting details : {}", entryAttributes);
            this.entryAttributes = entryAttributes;
            open = false;
        } else {
            throw new ImmutableEntryException(String.format("The entry : %s is closed, therefore cannot add more attributes", this));
        }
    }

    @Override
    public Cash getAmount() {

        log.debug("Returning unit amount : {} from accountingEntry : {}", amount, this);
        return amount;
    }

    @Override
    public TimePoint getBookingDate() {

        log.debug("Returning bookingDate : {} from accountingEntry : {}", bookingDate, this);
        return bookingDate;
    }

    private boolean isOpen() {

        log.debug("The entry is open ? : {}", open);
        return open;
    }

    /**
     * Only to be used by {@link AccountingTransaction}
     */
    @Override
    public void post() {

        forAccount.addEntry(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountingEntry that = (AccountingEntry) o;

        if (open != that.open) {
            return false;
        }
        if (entryAttributes != null ? !entryAttributes.equals(that.entryAttributes) : that.entryAttributes != null) {
            return false;
        }
        return (bookingDate != null ? bookingDate.equals(that.bookingDate) : that.bookingDate == null) && (forAccount != null ? forAccount.equals(that.forAccount) : that.forAccount == null) && (amount != null ? amount.equals(that.amount) : that.amount == null);
    }

    @Override
    public int hashCode() {
        int result = entryAttributes != null ? entryAttributes.hashCode() : 0;
        result = 31 * result + (open ? 1 : 0);
        result = 31 * result + (bookingDate != null ? bookingDate.hashCode() : 0);
        result = 31 * result + (forAccount != null ? forAccount.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountingEntry{");
        sb.append("entryDetails=").append(entryAttributes);
        sb.append(", open=").append(open);
        sb.append(", bookingDate=").append(bookingDate);
        sb.append(", forAccount=").append(forAccount);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
