package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountBalanceType;
import io.github.ghacupha.keeper.book.unit.money.Emonetary;
import io.github.ghacupha.keeper.book.unit.money.Emoney;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import io.github.ghacupha.keeper.book.unit.time.DateRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;

/**
 * This is a container for {@link java.util.Collection} of entries with the ability to return
 * {@link AccountBalance} on request.
 *
 * @author edwin.njeru
 */
public class AccountImpl implements Account{

    private static final Logger log = LoggerFactory.getLogger(AccountImpl.class);

    private final AccountBalanceType accountBalanceType;
    private Collection<Entry> entries = new HashSet<>();
    private final Currency currency;
    private final AccountDetails accountDetails;



    public AccountImpl(AccountBalanceType accountBalanceType, Currency currency, AccountDetails accountDetails) {
        this.accountBalanceType = accountBalanceType;
        this.currency = currency;
        this.accountDetails = accountDetails;

        log.debug("Account created : {}",this);
    }

    @Override
    public void addEntry(Entry entry) {

        log.debug("Adding entry to account : {}",entry);

        try {
            assertCurrency(Currency.getInstance(entry.getAmount().getCurrency().getCurrencyCode()));
            assertBookingDate(entry.getBookingDate());


            entries.add(entry); // done

        } catch (MismatchedCurrencyException e) {
            log.error("Exception encountered when adding amount : {} to account : {}", entry.getAmount(), accountDetails.getAccountName());
            e.printStackTrace();
        } catch (UntimelyBookingDateException e) {
            log.error("The booking date : {} is sooner than the account's opeing date : {}", entry.getBookingDate(),accountDetails.getOpeningDate());
            e.printStackTrace();
        }

    }

    private void assertBookingDate(TimePoint bookingDate) throws UntimelyBookingDateException{

        // booking date cannot be sooner than account opening date
        if(bookingDate.before(accountDetails.getOpeningDate())){
            String message = String.format("The booking date cannot be earlier than the account opening date :" +
                    "Opening date : %s . The entry date was %s", this.accountDetails.getOpeningDate(), bookingDate);
            throw new UntimelyBookingDateException(message);
        }
    }

    private void assertCurrency(Currency currency) throws MismatchedCurrencyException {

        if (!this.currency.equals(currency)) {

            String message = String.format("The monetary amount added is inconsistent with this account :" +
                    "Expected currency : %s but found %s", this.currency.toString(), currency.toString());
            throw new MismatchedCurrencyException(message);
        }
    }

    private AccountBalance balance(DateRange dateRange) {

        final Emonetary[] result = {new Emoney(0, currency)};

        entries.stream()
                .filter(entry -> dateRange.includes(entry.getBookingDate()))
                .map(filteredEntry -> {
                    Emonetary amount = filteredEntry.getAmount();
                    log.debug("Accounting entry : {} added into the balance with amount : {}",filteredEntry,amount);
                    return amount;
                }
                    )
                .forEachOrdered(orderedAmount -> {
                    log.debug("Adding amount : {}",orderedAmount);
                    result[0] = result[0].plus(orderedAmount);
                });

        log.debug("Returning balance of amount : {} on the {} side", result[0],accountBalanceType);

        return new AccountBalance(result[0], accountBalanceType);
    }

    @Override
    public AccountBalance balance(TimePoint asAt) {

        AccountBalance balance = balance(new DateRange(accountDetails.getOpeningDate(),asAt));

        log.debug("Returning accounting balance as at : {} as : {}",asAt,balance);

        return balance;
    }

    @Override
    public AccountBalance balance() {
        return balance(new TimePoint());
    }

    @Override
    public String toString() {
        return this.accountDetails.toString();
    }
}
