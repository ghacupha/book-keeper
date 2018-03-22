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

import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.balance.JournalSide;

import java.util.Collection;
import java.util.stream.Collectors;

import static io.github.ghacupha.keeper.book.balance.JournalSide.CREDIT;

/**
 * Representation of logic that the {@link Entry} items passed in the {@link Criteria#meetCriteria(Collection)}
 * method belong to the {@link JournalSide#CREDIT} side
 *
 * @author edwin.njeru
 */
public class EntryIsCreditCriteria implements Criteria<Entry>{


    /**
     * @param entries Input {@link Collection} of {@link Entry} items to be filtered
     * @return Output {@link Collection} of {@link Entry} items that belong to the {@link JournalSide#CREDIT} side
     */
    @Override
    public Collection<Entry> meetCriteria(Collection<Entry> entries) {

        return entries
                .stream()
                .filter(entry -> entry.getJournalSide()==CREDIT)
                .collect(ImmutableListCollector.toImmutableList());
    }
}
