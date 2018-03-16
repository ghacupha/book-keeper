package io.github.ghacupha.book_keeper;

import io.github.ghacupha.book_keeper.time.TimePoint;
import org.javamoney.moneta.Money;

/**
 * Collection of this {@link Entry} objects forms the {@link AccountImpl}, which is one of the
 * descriptors for an account
 * The {@link EntryDetails} is used as a descriptor, that could contain stuff like details, categories,
 * parties involved, invoice numbers etc
 *
 * @author edwin.njeru
 */
public interface Entry {

    Entry newEntry(AccountImpl account, EntryDetails entryDetails, Money amount, TimePoint bookingDate);

    EntryDetails getEntryDetails();

    void setEntryDetails(EntryDetails entryDetails) throws ImmutableEntryException;

    Money getAmount();

    TimePoint getBookingDate();
}
