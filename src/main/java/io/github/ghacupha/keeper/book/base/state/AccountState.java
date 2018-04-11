package io.github.ghacupha.keeper.book.base.state;

import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;

/**
 * Experimental representation of the states of the account
 */
public interface AccountState {

    /**
     * Get AccountBalance given the sum of debits and sum of credits
     * @param debits
     * @param credits
     * @return
     */
    AccountBalance getAccountBalance(Cash debits,Cash credits);

    /**
     *
     * @return {@code AccountSide} of the Account
     */
    AccountSide getAccountSide();
}
