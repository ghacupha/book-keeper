package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.balance.AccountBalanceType;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;

public class AccountTest {

    private Account account;

    @Before
    public void setUp() throws Exception {


        Moment openingDate = new Moment(2017,5,12);
        AccountDetails details = new AccountDetails("Electronics","001548418",openingDate);

        AccountBalanceType balanceType = AccountBalanceType.DR;

        account = new AccountImpl(balanceType, Currency.getInstance("KES"),details);
    }

    @Test
    public void addEntry() throws Exception {

        EntryAttributes details = new EntryDetails("Tuskys Supermarket invoice 10 Television set","for office","inv 10");
        Cash amount = HardCash.of(105.23,"KES");
        Entry entry = new AccountingEntry(account,details,amount,new Moment(2018,02,12));
        account.addEntry(entry);

        Assert.assertEquals(105.23,account.balance(new Moment()).getAmount().getNumber().doubleValue(),0.00);
    }



    @Test
    public void balance() throws Exception {

        EntryAttributes details = new EntryDetails("Tuskys Supermarket invoice 10 Television set","for office","inv 10");
        Cash amount = HardCash.of(105.23,"KES");
        Entry entry = new AccountingEntry(account,details,amount,new Moment(2018,02,12));
        account.addEntry(entry);

        EntryAttributes details2 = new EntryDetails("Tuskys Supermarket invoice 10 Fridge","for home","inv 12");
        Cash amount2 = HardCash.of(200.23,"KES");
        Entry entry2 = new AccountingEntry(account,details2,amount2,new Moment(2018,02,15));
        account.addEntry(entry2);

        Assert.assertEquals(305.46,account.balance(new Moment(2018,02,15)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(105.23,account.balance(new Moment(2018,02,13)).getAmount().getNumber().doubleValue(),0.00);
    }

}