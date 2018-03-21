[![Build Status](https://travis-ci.org/ghacupha/book-keeper.svg?branch=master)](https://travis-ci.org/ghacupha/book-keeper)

# book-keeper
Business accounts comprehension library for java.

The book-keeper implements Martin Fowler's Accounting Entry, Account and Accounting Transaction design patterns to create
accounting records of business transactions in any java program or game.

The Account and Entry interfaces, can be used on their own to track monetary traffic as shown 

```java
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
```

The book-keeper can also track entire transactions, each of which may contain several accounts as illustrated bellow : 

```java
public class AccountingTransactionTest {

    // Subscriptions expense journal
    Account subscriptionExpenseAccount;
    AccountAttributes subscriptionExpenseAccountAttributes;
    EntryAttributes subscriptionAccountEntryDetails;

    // Withhoding tax journal
    Account withholdingTaxAccount;
    AccountAttributes withholdingTaxAccountAttributes;
    EntryAttributes withholdingTaxDetailsEntry;

    // Banker's cheque suspense journal
    Account bankersChqAccountSuspense;
    AccountAttributes bankersChequeAccountDetails;
    EntryAttributes bankersChequeAccountEntry;


    @Before
    public void setUp() throws Exception {

        subscriptionExpenseAccountAttributes =
                new AccountDetails("Subscriptions","506",Moment.newMoment(2017,6,30));
        subscriptionExpenseAccount = new AccountImpl(DEBIT,Currency.getInstance("USD"),subscriptionExpenseAccountAttributes);
        subscriptionAccountEntryDetails = new EntryDetails("DSTV subscriptionAccountEntryDetails","Invoice# 1023","Approved in the budget",
                "MultiChoice Group Inc");

        withholdingTaxAccountAttributes =
                new AccountDetails("WithholdingTax","808",Moment.newMoment(2017,6,30));
        withholdingTaxAccount = new AccountImpl(CREDIT,Currency.getInstance("USD"),withholdingTaxAccountAttributes);
        withholdingTaxDetailsEntry = new EntryDetails("6% Withholding VAT","PIN#25646","Vendor under advisement","MultiChoice Group Inc");
        withholdingTaxDetailsEntry.setStringAttribute("Invoice#","1023");

        bankersChequeAccountDetails =
                AccountDetails.newDetails("Banker's Cheque A/C Suspense","303",Moment.newMoment(2017,6,30));
        bankersChqAccountSuspense = new AccountImpl(CREDIT,Currency.getInstance("USD"),bankersChequeAccountDetails);
        bankersChequeAccountEntry = EntryDetails.newDetails("BCHQ ifo MultiChoice Group","CHQ#5642","To print","MultiChoiceGroup Inc");
        bankersChequeAccountEntry.setStringAttribute("Bank Name","ABC Banks");
        bankersChequeAccountEntry.setStringAttribute("Bank Branch","WestLands");
        bankersChequeAccountEntry.setStringAttribute("Bank Branch Code","01");
    }

    @Test
    public void accountingTransactionWorks() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        Transaction transaction = new AccountingTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(HardCash.dollar(-800), subscriptionExpenseAccount, subscriptionAccountEntryDetails);
        transaction.add(HardCash.dollar(36),withholdingTaxAccount, withholdingTaxDetailsEntry);
        transaction.add(HardCash.dollar(764),bankersChqAccountSuspense,bankersChequeAccountEntry);

         transaction.post(); // Transaction must be posted to be effective

        assertEquals(AccountBalance.newBalance(HardCash.dollar(-800), DEBIT),subscriptionExpenseAccount.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(36), CREDIT),withholdingTaxAccount.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(764), CREDIT),bankersChqAccountSuspense.balance());
    }

    @Test
    public void unPostedAccountingTransactionFails() throws Exception, MismatchedCurrencyException, ImmutableEntryException, UnableToPostException {

        Transaction transaction =
                new AccountingTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(HardCash.dollar(-800), subscriptionExpenseAccount, subscriptionAccountEntryDetails);
        transaction.add(HardCash.dollar(36),withholdingTaxAccount, withholdingTaxDetailsEntry);
        transaction.add(HardCash.dollar(764),bankersChqAccountSuspense,bankersChequeAccountEntry);

        // Crickets...

        assertEquals(AccountBalance.newBalance(HardCash.dollar(0), DEBIT),subscriptionExpenseAccount.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(0), CREDIT),withholdingTaxAccount.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(0), CREDIT),bankersChqAccountSuspense.balance());
    }
}
```