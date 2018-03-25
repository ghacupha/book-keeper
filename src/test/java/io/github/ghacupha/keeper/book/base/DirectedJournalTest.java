package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.api.JournalizedTransaction;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.JournalSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.JournalSide.DEBIT;
import static org.junit.Assert.*;

public class DirectedJournalTest {

    // Subscriptions expense account
    private Account subscriptionExpenseJournal;
    private EntryAttributes subscriptionAccountEntryDetails;

    // Withholding tax account
    private Account withholdingTaxJournal;
    private EntryAttributes withholdingTaxDetailsEntry;

    // Banker's cheque suspense account
    private Account bankersChqJournalSuspense;
    private EntryAttributes bankersChequeAccountEntry;

    @Before
    public void setUp() throws Exception {

        // We are using directed journal instead
        AccountAttributes subscriptionExpenseAccountAttributes = new AccountDetails("Subscriptions", "506", Moment.newMoment(2017, 6, 30));
        subscriptionExpenseJournal = new DirectedJournal(DEBIT, Currency.getInstance("USD"), subscriptionExpenseAccountAttributes);
        subscriptionAccountEntryDetails = new EntryDetails("DSTV subscriptionAccountEntryDetails","Invoice# 1023","Approved in the budget",
                "MultiChoice Group Inc");

        AccountAttributes withholdingTaxAccountAttributes = new AccountDetails("WithholdingTax", "808", Moment.newMoment(2017, 6, 30));
        withholdingTaxJournal = new DirectedJournal(CREDIT,Currency.getInstance("USD"), withholdingTaxAccountAttributes);
        withholdingTaxDetailsEntry = new EntryDetails("6% Withholding VAT","PIN#25646","Vendor under advisement","MultiChoice Group Inc");
        withholdingTaxDetailsEntry.setStringAttribute("Invoice#","1023");

        AccountAttributes bankersChequeAccountDetails = AccountDetails.newDetails("Banker's Cheque A/C Suspense", "303", Moment.newMoment(2017, 6, 30));
        bankersChqJournalSuspense = new DirectedJournal(CREDIT,Currency.getInstance("USD"), bankersChequeAccountDetails);
        bankersChequeAccountEntry = EntryDetails.newDetails("BCHQ ifo MultiChoice Group","CHQ#5642","To print","MultiChoiceGroup Inc");
        bankersChequeAccountEntry.setStringAttribute("Bank Name","ABC Banks");
        bankersChequeAccountEntry.setStringAttribute("Bank Branch","WestLands");
        bankersChequeAccountEntry.setStringAttribute("Bank Branch Code","01");
    }

    @Test
    public void directedTransactionWorks() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        // Create the transaction
        JournalizedTransaction transaction = new DirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

        transaction.add(DEBIT,HardCash.dollar(800), subscriptionExpenseJournal, subscriptionAccountEntryDetails);
        transaction.add(CREDIT,HardCash.dollar(36), withholdingTaxJournal, withholdingTaxDetailsEntry);
        transaction.add(CREDIT,HardCash.dollar(764), bankersChqJournalSuspense,bankersChequeAccountEntry);

        transaction.post(); // Transaction must be posted to be effective


        assertEquals(AccountBalance.newBalance(HardCash.dollar(800), DEBIT), subscriptionExpenseJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(36), CREDIT), withholdingTaxJournal.balance());
        assertEquals(AccountBalance.newBalance(HardCash.dollar(764), CREDIT), bankersChqJournalSuspense.balance());
    }

    @Test
    public void unPostedAccountingTransactionFails() throws Exception, MismatchedCurrencyException, ImmutableEntryException, UnableToPostException {

        // Create the transaction
        JournalizedTransaction transaction = new DirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

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
        JournalizedTransaction transaction = new DirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

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
        JournalizedTransaction transaction = new DirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

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
        JournalizedTransaction transaction = new DirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

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
        JournalizedTransaction transaction = new DirectedTransaction(new Moment(2018,1,2), Currency.getInstance("USD"));

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