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

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;
import static org.junit.Assert.assertEquals;

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

        // Reversal Transaction
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