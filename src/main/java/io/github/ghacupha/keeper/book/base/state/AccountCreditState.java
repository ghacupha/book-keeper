package io.github.ghacupha.keeper.book.base.state;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;

import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;

public class AccountCreditState implements AccountState {

    private Account account;
    public AccountCreditState(Account account) {
        this.account = account;
    }

    /**
     * Get AccountBalance given the sum of debits and sum of credits
     *
     * @param debits
     * @param credits
     * @return
     */
    @Override
    public AccountBalance getAccountBalance(final Cash debits,final Cash credits) {

        if(credits.isMoreThan(debits)){
            return new AccountBalance(credits.minus(debits).abs(),account.getAccountSide());
        }

        // The journal side of the account changes to DEBIT
        account.setAccountSide(DEBIT);

        return new AccountBalance(credits.minus(debits).abs(),DEBIT);
    }

    /**
     * @return {@code AccountSide} of the Account
     */
    @Override
    public AccountSide getAccountSide() {

        return account.getAccountSide();
    }
}
