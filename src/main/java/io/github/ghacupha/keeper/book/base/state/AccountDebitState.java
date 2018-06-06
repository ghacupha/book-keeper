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
package io.github.ghacupha.keeper.book.base.state;

import io.github.ghacupha.cash.Cash;
import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;

/**
 * Represents the Account state when it is DEBIT state
 * 
 * @author edwin.njeru
 *
 */
public class AccountDebitState implements AccountState {

    private Account account;

    public AccountDebitState(Account account) {
        this.account = account;
    }

    /**
     * Get AccountBalance given the sum of debits and sum of credits
     *
     * @param debits Amount of {@link Cash} debits in the account
     * @param credits Amount of {@link Cash} credits in the account
     * @return {@link AccountBalance} of the {@link Account}
     */
    @Override
    public AccountBalance getAccountBalance(Cash debits, Cash credits) {

        if(debits.isMoreThan(credits)){
            return new AccountBalance(debits.minus(credits).abs(),account.getAccountSide());
        }

        account.setAccountSide(CREDIT);

        return new AccountBalance(credits.minus(debits).abs(),CREDIT);
    }

    @Override
    public AccountSide getAccountSide() {
        return account.getAccountSide();
    }

    @Override
    public String toString() {

        return "Debit state";
    }

    public Account getAccount() {
        return account;
    }
}
