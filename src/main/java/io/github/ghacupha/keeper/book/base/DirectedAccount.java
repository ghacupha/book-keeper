/*
 *  Copyright 2018 Edwin Njeru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.AccountBalance;
import io.github.ghacupha.keeper.book.balance.AccountSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import io.github.ghacupha.keeper.book.unit.time.TimePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;

import static io.github.ghacupha.keeper.book.balance.AccountSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.AccountSide.DEBIT;

/**
 * This implementation of the {@link Account} interface gives the balances for the entries as absolute
 * {@link AccountBalance} despite reversal in excess of {@link Cash} amount initially posted in the
 * direction of the side of the {@link Account}. When the sign changes the {@link AccountSide} of the
 * account is changed inorder to be more representative of the business domain.
 * The nature of this {@link Account} is such that it can belong to only one {@link Currency}
 *
 * @author edwin.njeru
 */
public class DirectedAccount extends SimpleAccountDecorator {

    private static final Logger log = LoggerFactory.getLogger(DirectedAccount.class);

    private AccountBalance accountBalance;

    /**
     * Creates {@link Account} instance with tracking for {@link AccountSide} for {@link Entry}
     * items added into the account
     *
     * @param accountSide       {@link AccountSide} to which the {@link Account} belongs. Indicates whether the {@link Account}
     *                          is on the {@link AccountSide#CREDIT} side or {@link AccountSide#DEBIT}
     * @param currency          {@link Currency} the {@link Account} is in. This {@link Account} can only have one {@link Currency}
     * @param accountAttributes {@link AccountAttributes} describing the designation and nature of the {@link Account}
     */
    public DirectedAccount(AccountSide accountSide, Currency currency, AccountAttributes accountAttributes) {
        super(accountSide, currency, accountAttributes);

        log.debug("DirectedAccount :{} created", this);
    }

    private static boolean entryIsDebitingUs(Entry entry) {
        return entry.getAccountSide() == DEBIT;
    }
    private static boolean entryIsCreditingUs(Entry entry) {
        return entry.getAccountSide() == CREDIT;
    }

    @Override
    public AccountBalance balance(TimePoint asAt) {

        log.debug("Account balance enquiry raised as at {}", asAt);

        Cash debits = getDebits();

        Cash credits = getCredits();

        if (debits.isZero() || credits.isZero()) {
            if(!debits.isZero() && credits.isZero()){
                return new AccountBalance(debits, DEBIT);
            } else if(debits.isZero() && !credits.isZero()){
                return new AccountBalance(credits, CREDIT);
            }
        } else if (this.getAccountSide() == DEBIT) {

            if (credits.isMoreThan(debits)) {
                return new AccountBalance(credits.minus(debits).abs(), CREDIT);
            }

            if (!credits.isMoreThan(debits)) {
                return new AccountBalance(credits.minus(debits).abs(), DEBIT);
            }
        } else if (this.getAccountSide() == CREDIT) {

            if (debits.isMoreThan(credits)) {
                return new AccountBalance(debits.minus(credits).abs(), DEBIT);
            }

            if (!debits.isMoreThan(credits)) {
                return new AccountBalance(debits.minus(credits).abs(), CREDIT);
            }
        }

        return null;

    }

    private Cash getCredits() {
        return HardCash.of(this.getEntries()
                .stream()
                .filter(DirectedAccount::entryIsCreditingUs)
                .map(entry -> entry.getAmount().getNumber().doubleValue())
                .reduce(0.00,(acc,value) -> acc + value), this.getCurrency());
    }

    private Cash getDebits() {
        return HardCash.of(this.getEntries()
                .stream()
                .filter(DirectedAccount::entryIsDebitingUs)
                .map(entry -> entry.getAmount().getNumber().doubleValue())
                .reduce(0.00,(acc,value) -> acc + value), this.getCurrency());
    }

    @Override
    public AccountBalance balance() {

        TimePoint enquiryDate = new Moment();

        log.debug("Account balance enquiry raised as at : {}", enquiryDate);

        AccountBalance retVal = balance(enquiryDate);

        log.debug("Balance as at : {} returned as : {}", retVal, retVal);

        return retVal;
    }

    @Override
    public String toString() {
        return "DirectedAccount {" + super.toString();
    }
}
