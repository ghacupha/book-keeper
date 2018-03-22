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

import java.util.Collection;

/**
 * This represents the logic of a both criteria being met at the same time
 *
 * @author edwin.njeru
 */
class CriteriaAndCondition<Candidate> implements Criteria<Candidate> {

    private Criteria<Candidate> criteria;
    private Criteria<Candidate> otherCriteria;

    CriteriaAndCondition(Criteria<Candidate> criteria, Criteria<Candidate> otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    /**
     * This performs the AND operation of 2 criteria results
     *
     * @param candidates Input {@link Collection} of {@link Candidate} items to be filtered
     * @return Output {@link Collection} of {@link Candidate} items that meet the {@link Criteria}
     */
    @Override
    public Collection<Candidate> meetCriteria(Collection<Candidate> candidates) {

        Collection<Candidate> firstCriteriaCollection = criteria.meetCriteria(candidates);

        return otherCriteria.meetCriteria(firstCriteriaCollection);
    }
}
