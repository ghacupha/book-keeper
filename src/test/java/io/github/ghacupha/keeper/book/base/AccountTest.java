package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.Transaction;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.SimpleDate;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;
import static org.junit.Assert.assertEquals;

public class AccountTest {

    private static final Logger log = LoggerFactory.getLogger(AccountTest.class);

    // Required for the reversal tests
    private Account advertisement = new SimpleAccount(DEBIT,Currency.getInstance("KES"),new AccountDetails("Advertisements","5280", SimpleDate.on(2017,3,31)));
    private Account vat = new SimpleAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("VAT","5281", SimpleDate.on(2017,3,31)));
    private Account chequeAccount = new SimpleAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("Cheque","5282", SimpleDate.on(2017,3,31)));
    private Account edwinsAccount = new SimpleAccount(CREDIT,Currency.getInstance("KES"),new AccountDetails("Edwin Njeru","40001", SimpleDate.on(2017,10,5)));



    @Test
    public void directedTransactionWorks() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        log.info("Testing if the transaction will work. First we create the pay for bills Transaction, using period 2017-11-2, and currency KES");
        Transaction payForBillBoards = new SimpleTransaction("BillboardsPayment",new SimpleDate(2017,11,2),Currency.getInstance("KES"));

        log.info("Done. We DEBIT the Advertisement account, and credit the VAT and Banker's Cheque accounts....");
        payForBillBoards.addEntry(DEBIT,HardCash.shilling(200),advertisement,new EntryDetails("Billboards ltd inv 10"));
        payForBillBoards.addEntry(CREDIT,HardCash.shilling(32),vat,new EntryDetails("VAT for billBoards"));
        payForBillBoards.addEntry(CREDIT,HardCash.shilling(168),chequeAccount,new EntryDetails("CHQ IFO Billboards Ltd"));

        // non-posted
        log.info("Ok so we have not yet posted the transaction but we want to check if the balances have been effected into the 3 accounts");
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),DEBIT),advertisement.balance(2018,1,3));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT),vat.balance(2018,1,3));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT),chequeAccount.balance(2018,1,3));

        // after posting
        log.info("Nothing? Good. So now lets post the transaction...");
        payForBillBoards.post();

        log.info("Posted. Now lets check if the balances in the account reflect our intentions");
        assertEquals(AccountBalance.newBalance(HardCash.shilling(200),DEBIT),advertisement.balance(2017,11,30));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2017,11,30));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2017,11,30));

        // Reimbursement Transaction
        log.info("Alright now we gotta reimburse Edwin for the meeting expenses, when he met with the Billboard guys. We create the" +
                " reimbursement transaction, as of 2017-12-20 in currency KES");
        Transaction reimbursement = new SimpleTransaction("Edwin\'s reimbursement",new SimpleDate(2017,12,20), Currency.getInstance("KES"));

        log.info("Alright, all we gotta do is debit the advertisement account and credit Edwin's account...");
        reimbursement.addEntry(DEBIT,HardCash.shilling(150),advertisement,new EntryDetails("Reimburse Edwin For Meeting expenses with Billboard guys"));
        reimbursement.addEntry(CREDIT,HardCash.shilling(150), edwinsAccount,new EntryDetails("Reimbursement for meeting expenses with billboard guys"));

        // before posting
        log.info("Again, we are going to check if the system has inappropriately added money into Edwin's account before our explicit posting...");
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT), edwinsAccount.balance(2017,12,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(200),DEBIT),advertisement.balance(2017,12,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2017,12,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2017,12,31));

        // after posting the reimbursement
        log.info("Nothing there. Good. Let us post the reimbursement transaction...");
        reimbursement.post();
        log.info("Done. Now lets check if the account have the balances we expect. Edwin should have 150 joys, advertisements should be at 350...");
        assertEquals(AccountBalance.newBalance(HardCash.shilling(150),CREDIT), edwinsAccount.balance(2018,1,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(350),DEBIT),advertisement.balance(2018,1,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2018,1,31));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2018,1,31));

        // what if the manager wants a previous position as at 5th November 2017
        log.info("Ok, this new strategy guy, for some obviously unholy reason wants to know our position as at 2017-11-05, time for some replay...");
        assertEquals(AccountBalance.newBalance(HardCash.shilling(0),CREDIT), edwinsAccount.balance(2017,11,5));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(200),DEBIT),advertisement.balance(2017,11,5));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(32),CREDIT),vat.balance(2017,11,5));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2017,11,5));

        // Someone screwed up the taxes, we have to reverse
        log.info("The internal audit reveals that someone had screwed up our taxes. Our taxes should be on the asset side by at least 13 joys. " +
                "Time to create some tax reversal transaction as at 2018-04-20, in KES as always");
        Transaction taxReversal = new SimpleTransaction("Tax reversal",SimpleDate.on(2018,4,20), Currency.getInstance("KES"));

        log.info("Adding entries to the tax reversal. We need to debit VAT by 45 joys and CREDIT advertisement expense by the same amount");
        taxReversal.addEntry(DEBIT,HardCash.shilling(45),vat,new EntryDetails("Reversal of Excess VAT"));
        taxReversal.addEntry(CREDIT,HardCash.shilling(45),advertisement,new EntryDetails("Reversal of Excess VAT"));

        log.info("We now post the tax reversal transaction");
        taxReversal.post();

        log.info("As per internal audit the VAT should be at 13 joys, asset side. Meaning the advertisement should be an expense of just 305");
        // balance after reversal transaction is posted...
        assertEquals(AccountBalance.newBalance(HardCash.shilling(150),CREDIT), edwinsAccount.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(305),DEBIT),advertisement.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(13),DEBIT),vat.balance(2018,4,25));
        assertEquals(AccountBalance.newBalance(HardCash.shilling(168),CREDIT),chequeAccount.balance(2018,4,25));
    }


}