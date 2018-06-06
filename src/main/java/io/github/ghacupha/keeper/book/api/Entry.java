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
package io.github.ghacupha.keeper.book.api;

import io.github.ghacupha.cash.Cash;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.base.EntryDetails;
import io.github.ghacupha.time.point.TimePoint;

import java.util.Currency;

/**
 * Unit of Account pattern implementation
 *
 * @author edwin.njeru
 */
public interface Entry {

    /**
     *
     * @return Description of the {@link Entry}
     */
    EntryDetails getEntryDetails();

    /**
     *
     * @return Currency of the monetary amounts to be save in this
     */
    Currency getCurrency();

    /**
     *
     * @return {@link AccountSide} to which this Entry is aggregating the
     * Account balance
     */
    AccountSide getAccountSide();

    /**
     *
     * @return Booking date of the Entry
     */
    TimePoint getBookingDate();

    /**
     *
     * @return The amount being posted into the Account and encapsulated
     * by the Entry
     */
    Cash getAmount();

    /**
     * Assigns this Entry with a specific account into which it is aggregated as
     * {@code AccountBalance}
     */
    void post();
}
