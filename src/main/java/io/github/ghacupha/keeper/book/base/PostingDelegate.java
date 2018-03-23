/*
 * Copyright 2018 Edwin Njeru
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
import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.JournalSide;
import io.github.ghacupha.keeper.book.unit.money.Cash;
import io.github.ghacupha.keeper.book.util.UnableToPostException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

import static io.github.ghacupha.keeper.book.balance.JournalSide.CREDIT;
import static io.github.ghacupha.keeper.book.balance.JournalSide.DEBIT;

class PostingDelegate implements AccountVisitor {

    private boolean wasPosted = false;
    private FixedJournalTransaction fixedJournalTransaction;

    PostingDelegate(FixedJournalTransaction fixedJournalTransaction) {
        this.fixedJournalTransaction = fixedJournalTransaction;
    }

    public void post(Collection<Account> accounts) throws UnableToPostException {

        if(!canPost()){

            throw new UnableToPostException();

        } else {

            accounts.forEach(account -> account.receive(this));

            wasPosted = true;

        }

    }

    public boolean canPost(){

        return balanced(fixedJournalTransaction.getEntries());
    }

    private boolean balanced(List<Entry> entries) {

       double debitEntries = entries
               .stream()
               .filter(entry -> entry.getJournalSide()== DEBIT)
               .map(entry -> entry.getAmount().getNumber().doubleValue())
               .reduce(0.00,(x,y)-> x + y);

       return debitEntries == entries
               .stream()
               .filter(entry -> entry.getJournalSide()==CREDIT)
               .map(entry -> entry.getAmount().getNumber().doubleValue())
               .reduce(0.00,(x,y) -> x + y);

    }

    @Override
    public void visit(VisitableAccount visitableAccount) {

    }
}
