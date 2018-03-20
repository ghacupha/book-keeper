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

package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.unit.time.TimePoint;

/**
 * Encapsulates information concerning the nature of an {@link AccountImpl} to which
 * it belongs
 *
 * @author edwin.njeru
 */
public class AccountDetails {

    private final String accountName;
    private final String accountNumber;
    private final TimePoint openingDate;

    public AccountDetails(String accountName, String accountNumber, TimePoint openingDate) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.openingDate = openingDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TimePoint getOpeningDate() {
        return openingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountDetails that = (AccountDetails) o;

        if (!accountName.equals(that.accountName)) {
            return false;
        }
        if (!accountNumber.equals(that.accountNumber)) {
            return false;
        }
        return openingDate.equals(that.openingDate);
    }

    @Override
    public int hashCode() {
        int result = accountName.hashCode();
        result = 31 * result + accountNumber.hashCode();
        result = 31 * result + openingDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountDetails{");
        sb.append("accountName='").append(accountName).append('\'');
        sb.append(", accountNumber='").append(accountNumber).append('\'');
        sb.append(", openingDate=").append(openingDate);
        sb.append('}');
        return sb.toString();
    }
}
