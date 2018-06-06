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

import io.github.ghacupha.keeper.book.util.UnEnteredDetailsException;
import io.github.ghacupha.time.point.TimePoint;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class AccountDetails {

    private final String name;

    private final String number;

    private final TimePoint openingDate;

    private final Map<String, Object> accountDetails = new ConcurrentHashMap<>();

    public AccountDetails(String name, String number, TimePoint openingDate) {
        this.name = name;
        this.number = number;
        this.openingDate = openingDate;
    }

    public static AccountDetails accountDetails(String name, String number, TimePoint openingDate){
        return new AccountDetails(name, number, openingDate);
    }

    String getName() {
        return name;
    }

    String getNumber() {
        return number;
    }

    TimePoint getOpeningDate() {
        return openingDate;
    }

    public void setAttribute(String label, Object attribute){
        accountDetails.put(label,attribute);
    }

    public Object getAttribute(String label) throws UnEnteredDetailsException {
        if(!accountDetails.containsKey(label)){
            throw new UnEnteredDetailsException(String.format("The attribute : %s is not found coz it was never entered in the first place",label));
        } else {
            return accountDetails.get(label);
        }
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
        return Objects.equals(name, that.name) && Objects.equals(number, that.number) && Objects.equals(openingDate, that.openingDate) && Objects.equals(accountDetails, that.accountDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, openingDate, accountDetails);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountDetails{");
        sb.append("name='").append(name).append('\'');
        sb.append(", number='").append(number).append('\'');
        sb.append(", openingDate=").append(openingDate);
        sb.append(", accountDetails=").append(accountDetails);
        sb.append('}');
        return sb.toString();
    }
}
