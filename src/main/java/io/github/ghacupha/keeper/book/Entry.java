package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.unit.money.Emonetary;
import io.github.ghacupha.keeper.book.unit.money.Emoney;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;

/**
 * Collection of this {@link Entry} objects forms the {@link AccountImpl}, which is one of the
 * descriptors for an account
 * The {@link EntryDetails} is used as a descriptor, that could contain stuff like details, categories,
 * parties involved, invoice numbers etc
 *
 * @author edwin.njeru
 */
public interface Entry {

    Entry newEntry(AccountImpl account, EntryDetails entryDetails, Emoney amount, TimePoint bookingDate);

    EntryDetails getEntryDetails();

    void setEntryDetails(EntryDetails entryDetails) throws ImmutableEntryException;

    Emonetary getAmount();

    TimePoint getBookingDate();
}
