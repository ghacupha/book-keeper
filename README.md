[![Build Status](https://travis-ci.org/ghacupha/book-keeper.svg?branch=master)](https://travis-ci.org/ghacupha/book-keeper)

# book-keeper
java library for managing business accounts

The library uses the Account class and Entry to track business transactions representing them in the double-entry fashion
like a shown: 

```
public class AccountTest {

    private Account account;

    @Before
    public void setUp() throws Exception {


        Moment openingDate = new Moment(2017,5,12);
        AccountAttributes details = new AccountDetails("Electronics","001548418",openingDate);

        AccountBalanceType balanceType = AccountBalanceType.DR;

        account = new AccountImpl(balanceType, Currency.getInstance("KES"),details);
    }

    @Test
    public void addEntry() throws Exception {

        EntryAttributes details = new EntryDetails("Tuskys Supermarket invoice 10 Television set","for office","inv 10");
        Cash amount = HardCash.of(105.23,"KES");
        Entry entry = new AccountingEntry(account,details,amount,new Moment(2018,2,12));
        account.addEntry(entry);

        Assert.assertEquals(105.23,account.balance(new Moment()).getAmount().getNumber().doubleValue(),0.00);
    }

    @Test
    public void balance() throws Exception {

        EntryAttributes details = new EntryDetails("Tuskys Supermarket invoice 10 Television set","for office","inv 10");
        Cash tvPrice = HardCash.of(105.23,"KES");
        Entry purchaseOfTV = new AccountingEntry(account,details,tvPrice,new Moment(2018,2,12));
        account.addEntry(purchaseOfTV);

        EntryAttributes details2 = new EntryDetails("Tuskys Supermarket invoice 10 Fridge","for home","inv 12");
        Cash amount2 = HardCash.of(200.23,"KES");
        Entry purchaseOfFridge = new AccountingEntry(account,details2,amount2,new Moment(2018,2,15));
        account.addEntry(purchaseOfFridge);

        EntryAttributes etrPurchaseDetails = new EntryDetails("Electronic Tax Register Machine");
        etrPurchaseDetails.setStringAttribute("Tax code","EY83E8");
        Cash etrPrice = HardCash.shilling(50.18);
        TimePoint etrPurchaseDate = new Moment(2018,3,1);
        Entry purchaseOfETR = new AccountingEntry(account,etrPurchaseDetails,etrPrice,etrPurchaseDate);
        account.addEntry(purchaseOfETR);

        Assert.assertEquals(305.46,account.balance(new Moment(2018,2,16)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(105.23,account.balance(new Moment(2018,2,13)).getAmount().getNumber().doubleValue(),0.00);
        Assert.assertEquals(355.64,account.balance().getAmount().getNumber().doubleValue(),0.00);
    }

}
```
