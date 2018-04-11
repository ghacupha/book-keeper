package io.github.ghacupha.keeper.book.base.state;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.base.SimpleAccount;
import io.github.ghacupha.keeper.book.base.state.AccountState;
import io.github.ghacupha.keeper.book.unit.money.Cash;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;

public class AccountDebitState implements AccountState {

    private Account account;

    public AccountDebitState(Account account) {
        this.account = account;
    }

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
