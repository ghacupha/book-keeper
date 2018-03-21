/*
 *  Copyright 2018 Edwin Njeru
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.EntryAttributes;
import io.github.ghacupha.keeper.book.api.Transaction;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.util.ImmutableEntryException;
import io.github.ghacupha.keeper.book.util.MismatchedCurrencyException;
import io.github.ghacupha.keeper.book.util.UnableToPostException;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.AccountBalanceType.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountBalanceType.DEBIT;
import static org.junit.Assert.assertEquals;

public class AccountingTransactionTest {

    // Subscriptions expense account
    Account subscriptionExpenseAccount;
    AccountAttributes subscriptionExpenseAccountAttributes;
    EntryAttributes subscriptionAccountEntryDetails;

    // Withhoding tax account
    Account withholdingTaxAccount;
    AccountAttributes withholdingTaxAccountAttributes;
    EntryAttributes withholdingTaxDetailsEntry;

    // Banker's cheque suspense account
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