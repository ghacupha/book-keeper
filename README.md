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

### Usage

```java
import static io.github.ghacupha.cash.HardCash.shilling;
import static io.github.ghacupha.keeper.book.balance.AccountBalance.newBalance;
import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;
import static io.github.ghacupha.keeper.book.base.AccountDetails.accountDetails;
import static io.github.ghacupha.keeper.book.base.EntryDetails.details;
import static io.github.ghacupha.keeper.book.base.SimpleAccount.makeAccount;
import static io.github.ghacupha.keeper.book.base.SimpleTransaction.getTransaction;
import static io.github.ghacupha.time.point.SimpleDate.on;

public class AccountTest {

    private static final Logger log = LoggerFactory.getLogger(AccountTest.class);

    // Currencies
    private static final Currency KES = Currency.getInstance("KES");

    // Required for the reversal tests
    private Account advertisement = makeAccount(DEBIT, KES, accountDetails("Advertisements", "5280", on(2017, 3, 31)));
    private Account vat = makeAccount(CREDIT, KES, accountDetails("VAT", "5281", on(2017, 3, 31)));
    private Account chequeAccount = makeAccount(CREDIT, KES, accountDetails("Cheque", "5282", on(2017, 3, 31)));
    private Account edwinsAccount = makeAccount(CREDIT, KES, accountDetails("Edwin Njeru", "40001", on(2017, 10, 5)));


    @Test
    public void directedTransactionWorks() throws Exception, UnableToPostException, MismatchedCurrencyException, ImmutableEntryException {

        log.info("\n Testing if the transaction will work. First we create the pay for bills Transaction, using period 2017-11-2, and currency KES");
        Transaction payForBillBoards = getTransaction("BillboardsPayment", new SimpleDate(2017, 11, 2), KES);

        log.info("\n Done. We DEBIT the Advertisement account, and credit the VAT and Banker's Cheque accounts....");
        payForBillBoards.addEntry(DEBIT, shilling(200), advertisement, details("Billboards ltd inv 10"));
        payForBillBoards.addEntry(CREDIT, shilling(32), vat, details("VAT for billBoards"));
        payForBillBoards.addEntry(CREDIT, shilling(168), chequeAccount, details("CHQ IFO Billboards Ltd"));

        // non-posted
        log.info("\n Ok so we have not yet posted the transaction but we want to check if the balances have been effected into the 3 accounts");
        assertEquals(newBalance(shilling(0), DEBIT), advertisement.balance(2018, 1, 3));
        assertEquals(newBalance(shilling(0), CREDIT), vat.balance(2018, 1, 3));
        assertEquals(newBalance(shilling(0), CREDIT), chequeAccount.balance(2018, 1, 3));

        // after posting
        log.info("\n Nothing? Good. So now lets post the transaction...");
        payForBillBoards.post();

        log.info("\n Posted. Now lets check if the balances in the account reflect our intentions");
        assertEquals(newBalance(shilling(200), DEBIT), advertisement.balance(2017, 11, 30));
        assertEquals(newBalance(shilling(32), CREDIT), vat.balance(2017, 11, 30));
        assertEquals(newBalance(shilling(168), CREDIT), chequeAccount.balance(2017, 11, 30));

        // Reimbursement Transaction
        log.info(
            "\n Alright now we gotta reimburse Edwin for the meeting expenses, when he met with the Billboard guys. We create the" + " reimbursement transaction, as of 2017-12-20 in currency KES");
        Transaction reimbursement = getTransaction("Edwin\'s reimbursement", new SimpleDate(2017, 12, 20), KES);

        log.info("\n Alright, all we gotta do is debit the advertisement account and credit Edwin's account...");
        reimbursement.addEntry(DEBIT, shilling(150), advertisement, details("Reimburse Edwin For Meeting expenses with Billboard guys"));
        reimbursement.addEntry(CREDIT, shilling(150), edwinsAccount, details("Reimbursement for meeting expenses with billboard guys"));

        // before posting
        log.info("\n Again, we are going to check if the system has inappropriately added money into Edwin's account before our explicit posting...");
        assertEquals(newBalance(shilling(0), CREDIT), edwinsAccount.balance(2017, 12, 31));
        assertEquals(newBalance(shilling(200), DEBIT), advertisement.balance(2017, 12, 31));
        assertEquals(newBalance(shilling(32), CREDIT), vat.balance(2017, 12, 31));
        assertEquals(newBalance(shilling(168), CREDIT), chequeAccount.balance(2017, 12, 31));

        // after posting the reimbursement
        log.info("Nothing there. Good. Let us post the reimbursement transaction...");
        reimbursement.post();
        log.info("Done. Now lets check if the account have the balances we expect. Edwin should have 150 joys, advertisements should be at 350...");
        assertEquals(newBalance(shilling(150), CREDIT), edwinsAccount.balance(2018, 1, 31));
        assertEquals(newBalance(shilling(350), DEBIT), advertisement.balance(2018, 1, 31));
        assertEquals(newBalance(shilling(32), CREDIT), vat.balance(2018, 1, 31));
        assertEquals(newBalance(shilling(168), CREDIT), chequeAccount.balance(2018, 1, 31));

        // what if the manager wants a previous position as at 5th November 2017
        log.info("\n Ok, this new strategy guy, for some obviously unholy reason wants to know our position as at 2017-11-05, time for some replay...");
        assertEquals(newBalance(shilling(0), CREDIT), edwinsAccount.balance(2017, 11, 5));
        assertEquals(newBalance(shilling(200), DEBIT), advertisement.balance(2017, 11, 5));
        assertEquals(newBalance(shilling(32), CREDIT), vat.balance(2017, 11, 5));
        assertEquals(newBalance(shilling(168), CREDIT), chequeAccount.balance(2017, 11, 5));

        // Someone screwed up the taxes, we have to reverse
        log.info("\n The internal audit reveals that someone had screwed up our taxes. Our taxes should be on the asset side by at least 13 joys. " +
            "Time to create some tax reversal transaction as at 2018-04-20, in KES as always");
        Transaction taxReversal = getTransaction("Tax reversal", on(2018, 4, 20), KES);

        log.info("\n Adding entries to the tax reversal. We need to debit VAT by 45 joys and CREDIT advertisement expense by the same amount");
        taxReversal.addEntry(DEBIT, shilling(45), vat, details("Reversal of Excess VAT"));
        taxReversal.addEntry(CREDIT, shilling(45), advertisement, details("Reversal of Excess VAT"));

        log.info("\n We now post the tax reversal transaction");
        taxReversal.post();

        log.info("\n As per internal audit the VAT should be at 13 joys, asset side. Meaning the advertisement should be an expense of just 305");
        // balance after reversal transaction is posted...
        assertEquals(newBalance(shilling(150), CREDIT), edwinsAccount.balance(2018, 4, 25));
        assertEquals(newBalance(shilling(305), DEBIT), advertisement.balance(2018, 4, 25));
        assertEquals(newBalance(shilling(13), DEBIT), vat.balance(2018, 4, 25));
        assertEquals(newBalance(shilling(168), CREDIT), chequeAccount.balance(2018, 4, 25));
    }


}
```

### Installation

The library is availbale as a snapshot. Just add the following to your pom

    <repositories>
       <repository>
        <id>sonatype-nexus-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        </repository>
    </repositories>
    
    <dependency>
        <groupId>io.github.ghacupha</groupId>
        <artifactId>book-keeper</artifactId>
        <version>1.2-SNAPSHOT</version>
     </dependency>
     


