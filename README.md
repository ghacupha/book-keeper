[![Build Status](https://travis-ci.org/ghacupha/book-keeper.svg?branch=master)](https://travis-ci.org/ghacupha/book-keeper)

# book-keeper
Business accounts comprehension library for java.

The book-keeper implements Martin Fowler's Accounting Entry, Account and Accounting Transaction design patterns to create
accounting records of business transactions in any java program or game.

The Account and Entry interfaces are used together to relate different account balance positions at different time periods. The
balance of the account is not signed, but the account side (Debit or Credit) to which the state of the account is in keeps
changing according to the entries posted.
The transaction interface is used to post multiple entries into multiple accounts at the same time ensuring that the money
is not created or destroyed (as per double-entry requirements), only transferred from one account to another. Again, money is
not created or destroyed, but transferred from one account to another. Any transaction must have accounts where we are debiting
and accounts we are crediting, and the debit entries must be equivalent to credit entries and in the same currency.

```java
public class DirectedSimpleAccountTest {

    // Required for the reversal tests
    Account advertisement = new SimpleAccount(DEBIT,Currency.getInstance("KES"),new AccountDetails("Advertisements","5280", SimpleDate.on(2017,3,31)));
    Account vat = new SimpleAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("VAT","5281", SimpleDate.on(2017,3,31)));
    Account chequeAccount = new SimpleAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("Cheque","5282", SimpleDate.on(2017,3,31)));
    Account edwinsAccount = new SimpleAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("Edwin Njeru","40001", SimpleDate.on(2017,10,5)));



    @Test
    public void directedTransactionWorks() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        Transaction payForBillBoards = new SimpleTransaction(new SimpleDate(2017,11,2),Currency.getInstance("KES"));

        payForBillBoards.addEntry(DEBIT,HardCash.shilling(200),advertisement,new EntryDetails("Billboards ltd inv 10"));
        payForBillBoards.addEntry(CREDIT,HardCash.shilling(32),vat,new EntryDetails("VAT for billBoards"));
        payForBillBoards.addEntry(CREDIT,HardCash.shilling(168),chequeAccount,new EntryDetails("CHQ IFO Billboards Ltd"));

        // non-posted
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),DEBIT),advertisement.balance(2018,1,3));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT),vat.balance(2018,1,3));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT),chequeAccount.balance(2018,1,3));

        // after posting
        payForBillBoards.post();
        assertEquals(AccountBalance.newBalance(HardCash.shilling(200),DEBIT),advertisement.balance(2017,11,30));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2017,11,30));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2017,11,30));

        // Reimbursement Transaction
        Transaction reimbursement = new SimpleTransaction(new SimpleDate(2017,12,20), Currency.getInstance("KES"));

        reimbursement.addEntry(DEBIT,HardCash.shilling(150),advertisement,new EntryDetails("Reimburse Edwin For Meeting expenses with Billboard guys"));
        reimbursement.addEntry(CREDIT,HardCash.shilling(150), edwinsAccount,new EntryDetails("Reimbursement for meeting expenses with billboard guys"));

        // before posting
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT), edwinsAccount.balance(2017,12,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(200),DEBIT),advertisement.balance(2017,12,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2017,12,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2017,12,31));

        // after posting the reimbursement
        reimbursement.post();
        assertEquals(AccountBalance.newBalance(HardCash.shilling(150),CREDIT), edwinsAccount.balance(2018,1,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(350),DEBIT),advertisement.balance(2018,1,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2018,1,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2018,1,31));

        // what if the manager wants a previous position as at 5th November 2017
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT), edwinsAccount.balance(2017,11,5));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(200),DEBIT),advertisement.balance(2017,11,5));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2017,11,5));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2017,11,5));

        // Someone screwed up the taxes, we have to reverse
        Transaction taxReversal = new SimpleTransaction(SimpleDate.on(2018,4,20), Currency.getInstance("KES"));

        taxReversal.addEntry(DEBIT,HardCash.shilling(45),vat,new EntryDetails("Reversal of Excess VAT"));
        taxReversal.addEntry(CREDIT,HardCash.shilling(45),advertisement,new EntryDetails("Reversal of Excess VAT"));

        taxReversal.post();

        // balance after reversal transaction is posted...
        assertEquals(AccountBalance.newBalance(HardCash.shilling(150),CREDIT), edwinsAccount.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(305),DEBIT),advertisement.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(13),DEBIT),vat.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2018,4,25));
    }
}
```

