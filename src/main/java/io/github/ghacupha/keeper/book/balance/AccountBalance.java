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

package io.github.ghacupha.keeper.book.balance;

import io.github.ghacupha.keeper.book.unit.money.Cash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the amount and sign of the amount you could find in an account
 *
 * @author edwin.njeru
 */
public class AccountBalance {

    private static final Logger log = LoggerFactory.getLogger(AccountBalance.class);

    private final Cash amount;
    private final AccountBalanceType accountBalanceType;

    public AccountBalance(Cash amount, AccountBalanceType accountBalanceType) {
        this.amount = amount;
        this.accountBalanceType = accountBalanceType;

        log.debug("AccountBalance created : {}", this);
    }

    private String balance() {

        return amount.getNumber().doubleValue() + "." + accountBalanceType;
    }

    public Cash getAmount() {
        return amount;
    }


    public AccountBalanceType getAccountBalanceType() {
        return accountBalanceType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountBalance that = (AccountBalance) o;

        if (!amount.equals(that.amount)) {
            return false;
        }
        return accountBalanceType == that.accountBalanceType;
    }

    @Override
    public int hashCode() {
        int result = amount.hashCode();
        result = 31 * result + accountBalanceType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return amount.getNumber().doubleValue() + "." + accountBalanceType;
    }


}
