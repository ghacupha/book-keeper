package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.internal.AccountDetails;
import io.github.ghacupha.keeper.book.internal.AccountImpl;
import io.github.ghacupha.keeper.book.internal.AccountingEntry;
import io.github.ghacupha.keeper.book.internal.EntryDetails;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.AccountBalanceType.DEBIT;

public class AccountTest {

    private Account electronicEquipmentAssetAccount;

    @Before
    public void setUp() throws Exception {
        TimePoint openingDate = Moment.newMoment(2017,5,12);
        AccountAttributes details = new AccountDetails("Electronics","001548418",openingDate);
        electronicEquipmentAssetAccount = new AccountImpl(DEBIT, Currency.getInstance("KES"),details);
    }

    @Test
    public void addEntry() throws Exception {

        EntryAttributes details = new EntryDetails("Keeper Supermarket invoice 10 Television set","Invoice#10","For Office");
        Cash amount = HardCash.of(105.23,"KES");
        Entry entry = new AccountingEntry(electronicEquipmentAssetAccount,details,amount,new Moment(2018,2,12));
        electronicEquipmentAssetAccount.addEntry(entry);

        Assert.assertEquals(105.23, electronicEquipmentAssetAccount.balance(new Moment()).getAmount().getNumber().doubleValue(),0.00);
    }

    @Test
    public void balance() throws Exception {

        EntryAttributes details = new EntryDetails("Keeper Supermarket invoice 10 Television set","Invoice#10","For Office");
        Cash tvPrice = HardCash.of(105.23,"KES");
        Entry purchaseOfTV = new AccountingEntry(electronicEquipmentAssetAccount,details,tvPrice,new Moment(2018,2,12));
        electronicEquipmentAssetAccount.addEntry(purchaseOfTV);

        EntryAttributes details2 = new EntryDetails("Keeper Supermarket invoice 12 Fridge","Invoice#12","For Home");
        Cash amount2 = HardCash.of(200.23,"KES");
        Entry purchaseOfFridge = new AccountingEntry(electronicEquipmentAssetAccount,details2,amount2,new Moment(2018,2,15));
        electronicEquipmentAssetAccount.addEntry(purchaseOfFridge);

        EntryAttributes etrPurchaseDetails = new EntryDetails("Electronic Tax Register Machine");
        etrPurchaseDetails.setStringAttribute("Tax code","EY83E8");
        Cash etrPrice = HardCash.shilling(50.18);
        TimePoint etrPurchaseDate = new Moment(2018,3,1);
        Entry purchaseOfETR = new AccountingEntry(electronicEquipmentAssetAccount,etrPurchaseDetails,etrPrice,etrPurchaseDate);
        electronicEquipmentAssetAccount.addEntry(purchaseOfETR);

        Assert.assertEquals(305.46, electronicEquipmentAssetAccount.balance(new Moment(2018,2,16)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(105.23, electronicEquipmentAssetAccount.balance(new Moment(2018,2,13)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(355.64, electronicEquipmentAssetAccount.balance().getAmount().getNumber().doubleValue(),0.00);
    }

}