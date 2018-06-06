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

import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;

/**
 * The Account could either be in {@link AccountSide#CREDIT} or {@code AccountSide#DEBT}
 * This interface maintains the methods common to all these states to allow
 * reuse
 * 
 * @author edwin.njeru
 */
public interface AccountState {

    /**
     * Get AccountBalance given the sum of debits and sum of credits
     * @param debits The amount of money on the debit side
     * @param credits The amount of money on the credit side
     * @return AccountBalance of the client
     */
    AccountBalance getAccountBalance(Cash debits,Cash credits);

    /**
     *
     * @return {@code AccountSide} of the Account
     */
    AccountSide getAccountSide();
}
