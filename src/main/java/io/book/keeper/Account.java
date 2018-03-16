package io.book.keeper;

import io.book.keeper.balance.AccountBalance;
import io.book.keeper.time.TimePoint;

/**
 * Forms a collection of related {@link Entry}. The {@link javax.money.CurrencyUnit} of the
 * {@link Entry} must the same with that of the {@link Account}
 *
 * @author edwin.njeru
 */
public interface Account {

    /**
     * Adds a new entry to the account
     *
     * @param entry
     */
    void addEntry(Entry entry);

    /**
     * Gives the account balance as at the {@link TimePoint} given
     *
     * @param asAt
     * @return
     */
    AccountBalance balance(TimePoint asAt);

    /**
     *
     * @return The {@link AccountBalance} as of now
     */
    AccountBalance balance();
}
