package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.balance.AccountBalanceType;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.javamoney.moneta.Money;
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

    /*@Test
    public void addEntry() throws Exception {

        EntryDetails details = new EntryDetails();
        Money amount = Money.of(105.23,"KES");
        Entry entry = new AccountingEntry(account,details,amount,new TimePoint(2018,02,12));
        account.addEntry(entry);

        Assert.assertEquals(105.23,account.balance(new TimePoint()).getAmount().getNumber().doubleValue(),0.00);
    }*/

    /*@Test
    public void MoneyTest() throws Exception {

        MonetaryAmount amount =  Money.of(12.00,"USD");
        MonetaryAmount fiveDollars =  Money.of(5.00,"USD");

        amount.add(fiveDollars);

        System.out.println(String.format("Money after add: %s",amount));

        assertTrue(Money.of(17,"USD").equals(amount));
    }*/

    /*@Test
    public void jodaMoneyTest() throws Exception {
        org.joda.unit.Money amount = org.joda.unit.Money.of(org.joda.unit.CurrencyUnit.getInstance("USD"),12);
        org.joda.unit.Money fiveDollars = org.joda.unit.Money.of(org.joda.unit.CurrencyUnit.getInstance("USD"),5);

        amount.plus(fiveDollars);

        System.out.println(amount);

        assertEquals(amount,org.joda.unit.Money.of(org.joda.unit.CurrencyUnit.getInstance("USD"),17));
    }*/

    @Test
    public void givenAmounts_whenSummed_thanCorrect() {
        MonetaryAmount[] monetaryAmounts = new MonetaryAmount[] {
                Money.of(100, "CHF"), Money.of(10.20, "CHF"), Money.of(1.15, "CHF")};

        Money sumAmtCHF = Money.of(0, "CHF");
        for (MonetaryAmount monetaryAmount : monetaryAmounts) {
            sumAmtCHF = sumAmtCHF.add(monetaryAmount);
        }

        assertEquals("CHF 111.35", sumAmtCHF.toString());
    }

    @Test
    public void balance() throws Exception {
    }

    @Test
    public void balance1() throws Exception {
    }

}