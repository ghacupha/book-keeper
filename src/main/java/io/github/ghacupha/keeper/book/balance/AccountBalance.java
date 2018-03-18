package io.github.ghacupha.keeper.book.balance;

import io.github.ghacupha.keeper.book.unit.money.Emonetary;
import io.github.ghacupha.keeper.book.unit.money.Emoney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the amount and sign of the amount you could find in an account
 *
 * @author edwin.njeru
 */
public class AccountBalance {

    private static final Logger log = LoggerFactory.getLogger(AccountBalance.class);

    private final Emonetary amount;
    private final AccountBalanceType accountBalanceType;

    public AccountBalance(Emoney amount, AccountBalanceType accountBalanceType) {
        this.amount = amount;
        this.accountBalanceType = accountBalanceType;

        log.debug("AccountBalance created : {}",this);
    }

    private String balance(){

        return amount.getNumber().doubleValue() + "."+accountBalanceType;
    }

    public Emonetary getAmount() {
        return amount;
    }


    public AccountBalanceType getAccountBalanceType() {
        return accountBalanceType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountBalance that = (AccountBalance) o;

        if (!amount.equals(that.amount)) return false;
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
       return amount.getNumber().doubleValue() + "."+accountBalanceType;
    }


}
