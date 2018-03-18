package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.unit.money.Emonetary;
import io.github.ghacupha.keeper.book.unit.money.Emoney;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountingEntry implements Entry {

    private static final Logger log = LoggerFactory.getLogger(AccountingEntry.class);

    private EntryDetails entryDetails;
    private boolean open;
    private TimePoint bookingDate;
    private Account forAccount;
    private Emonetary amount;

    AccountingEntry(Account forAccount, EntryDetails entryDetails, Emonetary amount, TimePoint bookingDate) {
        this.bookingDate=bookingDate;
        this.open=true;
        this.amount=amount;
        try {
            this.setEntryDetails(entryDetails);
        } catch (ImmutableEntryException e) {
            e.printStackTrace();
        }
        this.forAccount=forAccount;
        this.bookingDate=bookingDate;

        log.debug("Accounting entry created : {}",this);
    }

    @Override
    public Entry newEntry(AccountImpl account, EntryDetails entryDetails, Emoney amount, TimePoint bookingDate) {

        return new AccountingEntry(account,entryDetails,amount,bookingDate);

    }

    @Override
    public EntryDetails getEntryDetails() {
        log.debug("Returning entry details : {}",entryDetails);

        return entryDetails;
    }

    @Override
    public Emonetary getAmount() {

        log.debug("Returning unit amount : {} from accountingEntry : {}",amount,this);
        return amount;
    }

    @Override
    public TimePoint getBookingDate() {

        log.debug("Returning bookingDate : {} from accountingEntry : {}",bookingDate,this);
        return bookingDate;
    }

    @Override
    public void setEntryDetails(EntryDetails entryDetails) throws ImmutableEntryException {

        log.debug("Setting EntryDetails : {}",entryDetails);
        if (isOpen()) {

            log.debug("Entry is open. Setting details : {}",entryDetails);
            this.entryDetails = entryDetails;
            open=false;
        }
        else
            throw new ImmutableEntryException();
    }

    private boolean isOpen() {

        log.debug("The entry is open ? : {}",open);
        return open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountingEntry that = (AccountingEntry) o;

        if (open != that.open) return false;
        if (entryDetails != null ? !entryDetails.equals(that.entryDetails) : that.entryDetails != null) return false;
        if (bookingDate != null ? !bookingDate.equals(that.bookingDate) : that.bookingDate != null) return false;
        if (forAccount != null ? !forAccount.equals(that.forAccount) : that.forAccount != null) return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;
    }

    @Override
    public int hashCode() {
        int result = entryDetails != null ? entryDetails.hashCode() : 0;
        result = 31 * result + (open ? 1 : 0);
        result = 31 * result + (bookingDate != null ? bookingDate.hashCode() : 0);
        result = 31 * result + (forAccount != null ? forAccount.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountingEntry{");
        sb.append("entryDetails=").append(entryDetails);
        sb.append(", open=").append(open);
        sb.append(", bookingDate=").append(bookingDate);
        sb.append(", forAccount=").append(forAccount);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
