package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UntimelyBookingDateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;

public class SimpleAccountTest {

    private SimpleAccount electronicEquipmentAssetSimpleAccount;

    @Before
    public void setUp() throws Exception ,UntimelyBookingDateException, MismatchedCurrencyException{
        TimePoint openingDate = Moment.newMoment(2017,5,12);
        AccountAttributes details = new AccountDetails("Electronics","001548418",openingDate);
        electronicEquipmentAssetSimpleAccount = new SimpleAccount(DEBIT, Currency.getInstance("KES"),details);
    }

    @Test
    public void addEntry() throws Exception,UntimelyBookingDateException, MismatchedCurrencyException {

        EntryAttributes details = new EntryDetails("Keeper Supermarket invoice 10 Television set","Invoice#10","For Office");
        Cash amount = HardCash.of(105.23,"KES");
        Entry entry = new AccountingEntry(electronicEquipmentAssetSimpleAccount,details,amount,new Moment(2018,2,12));
        electronicEquipmentAssetSimpleAccount.addEntry(entry);

        Assert.assertEquals(105.23, electronicEquipmentAssetSimpleAccount.balance(new Moment()).getAmount().getNumber().doubleValue(),0.00);
    }

    @Test
    public void balance() throws Exception,UntimelyBookingDateException, MismatchedCurrencyException {

        EntryAttributes details = new EntryDetails("Keeper Supermarket invoice 10 Television set","Invoice#10","For Office");
        Cash tvPrice = HardCash.of(105.23,"KES");
        Entry purchaseOfTV = new AccountingEntry(electronicEquipmentAssetSimpleAccount,details,tvPrice,new Moment(2018,2,12));
        electronicEquipmentAssetSimpleAccount.addEntry(purchaseOfTV);

        EntryAttributes details2 = new EntryDetails("Keeper Supermarket invoice 12 Fridge","Invoice#12","For Home");
        Cash amount2 = HardCash.of(200.23,"KES");
        Entry purchaseOfFridge = new AccountingEntry(electronicEquipmentAssetSimpleAccount,details2,amount2,new Moment(2018,2,15));
        electronicEquipmentAssetSimpleAccount.addEntry(purchaseOfFridge);

        EntryAttributes etrPurchaseDetails = new EntryDetails("Electronic Tax Register Machine");
        etrPurchaseDetails.setStringAttribute("Tax code","EY83E8");
        Cash etrPrice = HardCash.shilling(50.18);
        TimePoint etrPurchaseDate = new Moment(2018,3,1);
        Entry purchaseOfETR = new AccountingEntry(electronicEquipmentAssetSimpleAccount,etrPurchaseDetails,etrPrice,etrPurchaseDate);
        electronicEquipmentAssetSimpleAccount.addEntry(purchaseOfETR);

        Assert.assertEquals(305.46, electronicEquipmentAssetSimpleAccount.balance(new Moment(2018,2,16)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(105.23, electronicEquipmentAssetSimpleAccount.balance(new Moment(2018,2,13)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(355.64, electronicEquipmentAssetSimpleAccount.balance().getAmount().getNumber().doubleValue(),0.00);
    }

    @Test(expected = UntimelyBookingDateException.class)
    public void entriesCannotBeBookedBeforeTheAccountIsOpened() throws Exception, UntimelyBookingDateException, MismatchedCurrencyException {

        EntryAttributes details = new EntryDetails("Keeper Supermarket invoice 10 Television set","Invoice#10","For Office");
        Cash amount = HardCash.of(105.23,"KES");
        Entry entry = new AccountingEntry(electronicEquipmentAssetSimpleAccount,details,amount,Moment.newMoment(2017,4,12));
        electronicEquipmentAssetSimpleAccount.addEntry(entry);

        Assert.assertEquals(105.23, electronicEquipmentAssetSimpleAccount.balance(new Moment()).getAmount().getNumber().doubleValue(),0.00);
    }

    @Test(expected = MismatchedCurrencyException.class)
    public void entriesWithMismatchedCurrenciesCannotBeBooked() throws Exception, UntimelyBookingDateException, MismatchedCurrencyException {

        EntryAttributes details = new EntryDetails("Keeper Supermarket invoice 10 Television set","Invoice#10","For Office");
        Cash amount = HardCash.euro(105.23);
        Entry entry = new AccountingEntry(electronicEquipmentAssetSimpleAccount,details,amount,Moment.newMoment(2017,8,12));
        electronicEquipmentAssetSimpleAccount.addEntry(entry);

        Assert.assertEquals(105.23, electronicEquipmentAssetSimpleAccount.balance(new Moment()).getAmount().getNumber().doubleValue(),0.00);
    }

}