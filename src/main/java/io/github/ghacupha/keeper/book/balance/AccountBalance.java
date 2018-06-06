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
package io.github.ghacupha.keeper.book.balance;

import io.github.ghacupha.cash.Cash;

/**
 * Represents the amount and sign of the amount you could find in an account. Using this
 * you could say an account has 20 dollars on Credit side.
 *
 * @author edwin.njeru
 */
public class AccountBalance {

    private final Cash amount;
    private final AccountSide accountSide;

    public AccountBalance(Cash amount, AccountSide accountSide) {
        this.amount = amount;
        this.accountSide = accountSide;
    }

    public static AccountBalance newBalance(Cash amount, AccountSide accountSide) {

        return new AccountBalance(amount, accountSide);
    }

    public Cash getAmount() {
        return amount;
    }

    public AccountSide getAccountSide() {
        return accountSide;
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
        return accountSide == that.accountSide;
    }

    @Override
    public int hashCode() {
        int result = amount.hashCode();
        result = 31 * result + accountSide.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return amount.getNumber().doubleValue() + " " + accountSide;
    }
}
