package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.balance.AccountBalanceType;
import io.github.ghacupha.keeper.book.unit.money.Emonetary;
import io.github.ghacupha.keeper.book.unit.money.Emoney;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Currency;

import static org.junit.Assert.*;

public class AccountTest {

    private Account account;

    @Before
    public void setUp() throws Exception {


        TimePoint openingDate = new TimePoint(2017,5,12);
        AccountDetails details = new AccountDetails("Electronics","001548418",openingDate);

        AccountBalanceType balanceType = AccountBalanceType.DR;

        CurrencyUnit currency = CurrencyUnitBuilder.of("KES","KES").setCurrencyCode("KES").build();

        account = new AccountImpl(balanceType, Currency.getInstance("KES"),details);
    }

    @Test
    public void addEntry() throws Exception {

        EntryDetails details = new EntryDetails("Tuskys Supermarket invoice 10 Television set","for office","inv 10");
        Emonetary amount = Emoney.of(105.23,"KES");
        Entry entry = new AccountingEntry(account,details,amount,new TimePoint(2018,02,12));
        account.addEntry(entry);

        Assert.assertEquals(105.23,account.balance(new TimePoint()).getAmount().getNumber().doubleValue(),0.00);
    }



    @Test
    public void balance() throws Exception {

        EntryDetails details = new EntryDetails("Tuskys Supermarket invoice 10 Television set","for office","inv 10");
        Emonetary amount = Emoney.of(105.23,"KES");
        Entry entry = new AccountingEntry(account,details,amount,new TimePoint(2018,02,12));
        account.addEntry(entry);

        EntryDetails details2 = new EntryDetails("Tuskys Supermarket invoice 10 Fridge","for home","inv 12");
        Emonetary amount2 = Emoney.of(200.23,"KES");
        Entry entry2 = new AccountingEntry(account,details2,amount2,new TimePoint(2018,02,15));
        account.addEntry(entry2);

        Assert.assertEquals(305.46,account.balance(new TimePoint(2018,02,15)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(105.23,account.balance(new TimePoint(2018,02,13)).getAmount().getNumber().doubleValue(),0.00);
    }

}