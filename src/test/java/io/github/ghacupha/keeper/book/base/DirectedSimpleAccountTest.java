package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.DirectedTransaction;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;
import static org.junit.Assert.*;

public class DirectedSimpleAccountTest {

    // Subscriptions expense account
    private Account subscriptionExpenseJournal;
    private EntryAttributes subscriptionAccountEntryDetails;

    // Withholding tax account
    private Account withholdingTaxJournal;
    private EntryAttributes withholdingTaxDetailsEntry;

    // Banker's cheque suspense account
    private Account bankersChqJournalSuspense;
    private EntryAttributes bankersChequeAccountEntry;

    // Required for the reversal tests
    Account advertisement = new DirectedAccount(DEBIT,Currency.getInstance("KES"),new AccountDetails("Advertisements","5280",Moment.on(2017,3,31)));
    Account vat = new DirectedAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("VAT","5281",Moment.on(2017,3,31)));
    Account chequeAccount = new DirectedAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("Cheque","5282",Moment.on(2017,3,31)));
    Account edwinsAccount = new DirectedAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("Edwin Njeru","40001",Moment.on(2017,10,5)));

    @Before
    public void setUp() throws Exception {

        // We are using directed journal instead
        AccountAttributes subscriptionExpenseAccountAttributes = new AccountDetails("Subscriptions", "506", Moment.newMoment(2017, 6, 30));
        subscriptionExpenseJournal = new DirectedAccount(DEBIT, Currency.getInstance("USD"), subscriptionExpenseAccountAttributes);
        subscriptionAccountEntryDetails = new EntryDetails("DSTV subscriptionAccountEntryDetails","Invoice# 1023","Approved in the budget",
                "MultiChoice Group Inc");

        AccountAttributes withholdingTaxAccountAttributes = new AccountDetails("WithholdingTax", "808", Moment.newMoment(2017, 6, 30));
        withholdingTaxJournal = new DirectedAccount(CREDIT,Currency.getInstance("USD"), withholdingTaxAccountAttributes);
        withholdingTaxDetailsEntry = new EntryDetails("6% Withholding VAT","PIN#25646","Vendor under advisement","MultiChoice Group Inc");
        withholdingTaxDetailsEntry.setStringAttribute("Invoice#","1023");

        AccountAttributes bankersChequeAccountDetails = AccountDetails.newDetails("Banker's Cheque A/C Suspense", "303", Moment.newMoment(2017, 6, 30));
        bankersChqJournalSuspense = new DirectedAccount(CREDIT,Currency.getInstance("USD"), bankersChequeAccountDetails);
        bankersChequeAccountEntry = EntryDetails.newDetails("BCHQ ifo MultiChoice Group","CHQ#5642","To print","MultiChoiceGroup Inc");
        bankersChequeAccountEntry.setStringAttribute("Bank Name","ABC Banks");
        bankersChequeAccountEntry.setStringAttribute("Bank Branch","WestLands");
        bankersChequeAccountEntry.setStringAttribute("Bank Branch Code","01");
    }

    @Test
    public void directedTransactionWorks() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        DirectedTransaction payForBillBoards = new SimpleDirectedTransaction(new Moment(2017,11,2), Currency.getInstance("KES"));

        payForBillBoards.add(DEBIT,HardCash.shilling(200),advertisement,new EntryDetails("Billboards ltd inv 10"));
        payForBillBoards.add(CREDIT,HardCash.shilling(32),vat,new EntryDetails("VAT for billBoards"));
        payForBillBoards.add(CREDIT,HardCash.shilling(168),chequeAccount,new EntryDetails("CHQ IFO Billboards Ltd"));

        // non-posted
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),DEBIT),advertisement.balance(2018,1,3));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT),vat.balance(2018,1,3));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT),chequeAccount.balance(2018,1,3));

        // after posting
        payForBillBoards.post();
        assertEquals(AccountBalance.newBalance(HardCash.shilling(200),DEBIT),advertisement.balance(2017,11,30));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2017,11,30));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2017,11,30));

        // Reversal Transaction
        DirectedTransaction reimbursement = new SimpleDirectedTransaction(new Moment(2017,12,20), Currency.getInstance("KES"));

        reimbursement.add(DEBIT,HardCash.shilling(150),advertisement,new EntryDetails("Reimburse Edwin For Meeting expenses with Billboard guys"));
        reimbursement.add(CREDIT,HardCash.shilling(150), edwinsAccount,new EntryDetails("Reimbursement for meeting expenses with billboard guys"));

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
        DirectedTransaction taxReversal = new SimpleDirectedTransaction(Moment.on(2018,4,20),Currency.getInstance("KES"));

        taxReversal.add(DEBIT,HardCash.shilling(45),vat,new EntryDetails("Reversal of Excess VAT"));
        taxReversal.add(CREDIT,HardCash.shilling(45),advertisement,new EntryDetails("Reversal of Excess VAT"));

        taxReversal.post();

        // balance after reversal transaction is posted...
        assertEquals(AccountBalance.newBalance(HardCash.shilling(150),CREDIT), edwinsAccount.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(305),DEBIT),advertisement.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(13),DEBIT),vat.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2018,4,25));
    }

    /*@Test
    public void accountDirectionCanSwitchAccordingly() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        DirectedTransaction transaction = new SimpleDirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(DEBIT,HardCash.dollar(80), subscriptionExpenseJournal, subscriptionAccountEntryDetails);
        transaction.add(CREDIT,HardCash.dollar(3.6), withholdingTaxJournal, withholdingTaxDetailsEntry);
        transaction.add(CREDIT,HardCash.dollar(76.4), bankersChqJournalSuspense,bankersChequeAccountEntry);

        transaction.post(); // Transaction must be posted to be effective


        assertEquals(AccountBalance.newBalance(HardCash.dollar(80), DEBIT), subscriptionExpenseJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(3.6), CREDIT), withholdingTaxJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(76.4), CREDIT), bankersChqJournalSuspense.balance());

        // some reversal
        DirectedTransaction reversalTransaction = new SimpleDirectedTransaction(new Moment(2018,1,5), Currency.getInstance("USD"));

        reversalTransaction.add(CREDIT,HardCash.dollar(90),subscriptionExpenseJournal,new EntryDetails("Year 2018 zerolization"));
        reversalTransaction.add(DEBIT,HardCash.dollar(10),withholdingTaxJournal,new EntryDetails("Year 2018 zerolization"));
        reversalTransaction.add(DEBIT,HardCash.dollar(80),bankersChqJournalSuspense,new EntryDetails("Year 2018 zerolization"));

        reversalTransaction.post();

        assertEquals(AccountBalance.newBalance(HardCash.dollar(10), CREDIT), subscriptionExpenseJournal.balance(2018,1,6));
        assertEquals(AccountBalance.newBalance(HardCash.dollar(6.4), DEBIT), withholdingTaxJournal.balance(2018,1,6));
        assertEquals(AccountBalance.newBalance(HardCash.dollar(3.6), DEBIT), bankersChqJournalSuspense.balance(2018,1,6));
    }*/

    @Test
    public void unPostedAccountingTransactionFails() throws Exception, MismatchedCurrencyException, ImmutableEntryException, UnableToPostException {

        // Create the transaction
        DirectedTransaction transaction = new SimpleDirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(DEBIT,HardCash.dollar(800), subscriptionExpenseJournal, subscriptionAccountEntryDetails);
        transaction.add(CREDIT,HardCash.dollar(36), withholdingTaxJournal, withholdingTaxDetailsEntry);
        transaction.add(CREDIT,HardCash.dollar(764), bankersChqJournalSuspense,bankersChequeAccountEntry);

        // crickets...

        // All accounts remain at Zero balance
        assertEquals(AccountBalance.newBalance(HardCash.dollar(0), DEBIT), subscriptionExpenseJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(0), CREDIT), withholdingTaxJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(0), CREDIT), bankersChqJournalSuspense.balance());
    }

    @Test(expected = ImmutableEntryException.class)
    public void transactionsCannotBeAddedAfterPosting() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        DirectedTransaction transaction = new SimpleDirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(DEBIT,HardCash.dollar(800), subscriptionExpenseJournal, subscriptionAccountEntryDetails);
        transaction.add(CREDIT,HardCash.dollar(36), withholdingTaxJournal, withholdingTaxDetailsEntry);
        transaction.add(CREDIT,HardCash.dollar(764), bankersChqJournalSuspense,bankersChequeAccountEntry);

        transaction.post();

        //  Oops we needed to add one more cheque to balance the transaction :)
        transaction.add(CREDIT,HardCash.dollar(400), bankersChqJournalSuspense,bankersChequeAccountEntry);


        assertEquals(AccountBalance.newBalance(HardCash.dollar(800), DEBIT), subscriptionExpenseJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(36), CREDIT), withholdingTaxJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(764), CREDIT), bankersChqJournalSuspense.balance());
    }

    @Test(expected = UnableToPostException.class)
    public void creditsCannotBeMoreThanDebits() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        DirectedTransaction transaction = new SimpleDirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(DEBIT,HardCash.dollar(780), subscriptionExpenseJournal, subscriptionAccountEntryDetails);
        transaction.add(CREDIT,HardCash.dollar(36), withholdingTaxJournal, withholdingTaxDetailsEntry);
        transaction.add(CREDIT,HardCash.dollar(764), bankersChqJournalSuspense,bankersChequeAccountEntry);

        transaction.post();


        assertEquals(AccountBalance.newBalance(HardCash.dollar(800), DEBIT), subscriptionExpenseJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(36), CREDIT), withholdingTaxJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(764), CREDIT), bankersChqJournalSuspense.balance());
    }

    @Test(expected = UnableToPostException.class)
    public void debitsCannotBeMoreThanCredits() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        DirectedTransaction transaction = new SimpleDirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(DEBIT,HardCash.dollar(800), subscriptionExpenseJournal, subscriptionAccountEntryDetails);
        transaction.add(CREDIT,HardCash.dollar(36), withholdingTaxJournal, withholdingTaxDetailsEntry);
        transaction.add(CREDIT,HardCash.dollar(710), bankersChqJournalSuspense,bankersChequeAccountEntry);

        transaction.post(); // Transaction must be posted to be effective


        assertEquals(AccountBalance.newBalance(HardCash.dollar(800), DEBIT), subscriptionExpenseJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(36), CREDIT), withholdingTaxJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(764), CREDIT), bankersChqJournalSuspense.balance());
    }

    @Test(expected = MismatchedCurrencyException.class)
    public void mismatchedEntryCurrenciesCannotBePosted() throws Exception, MismatchedCurrencyException, ImmutableEntryException, UnableToPostException {

        // Create the transaction
        DirectedTransaction transaction = new SimpleDirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(DEBIT,HardCash.dollar(800), subscriptionExpenseJournal, subscriptionAccountEntryDetails);
        transaction.add(CREDIT,HardCash.sterling(36), withholdingTaxJournal, withholdingTaxDetailsEntry);
        transaction.add(CREDIT,HardCash.dollar(764), bankersChqJournalSuspense,bankersChequeAccountEntry);

        transaction.post();

        // relax! we won't even reach here
        assertEquals(AccountBalance.newBalance(HardCash.dollar(800), DEBIT), subscriptionExpenseJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.sterling(36), CREDIT), withholdingTaxJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(764), CREDIT), bankersChqJournalSuspense.balance());
    }



}