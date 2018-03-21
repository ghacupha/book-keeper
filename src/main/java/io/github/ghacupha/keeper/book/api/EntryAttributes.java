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

package io.github.ghacupha.keeper.book.api;

import io.github.ghacupha.keeper.book.unit.money.Cash;

/**
 * Provide data that describe the details that are related to an {@link Entry}.
 *
 * @author edwin.njeru
 */
public interface EntryAttributes {
    /**
     * @return Narration for the entry made in the account
     */
    String getNarration();

    /**
     * @return Additional remarks made in the account iif any
     */
    String getRemarks();

    /**
     * @return referenceNumber in regard to the entry, could be debit note number or invoice
     * number
     */
    String getReferenceNumber();

    /**
     * @return Name of the party with whom the concern is conducting business
     */
    String getParty();

    /**
     * @param attribute Attribute name as String
     * @param value     Value for the attribute as String
     */
    void setStringAttribute(String attribute, String value);

    /**
     * @param cashAttribute Attribute name as String
     * @param value         Value for the attribute as {@link Cash}
     */
    void setCashAttribute(String cashAttribute, Cash value);

    /**
     * @param objectAttribute Attribute name as String
     * @param value           Value for the attribute as any {@link Object}
     */
    void setObjectAttribute(String objectAttribute, Object value);

    /**
     * @param attribute String item saved in the {@link Entry}
     * @return String item saved in the {@link Entry}
     */
    String getStringAttribute(String attribute);

    /**
     * @param attribute String item saved in the {@link Entry}
     * @return {@link Cash} item saved in the {@link Entry}
     */
    Cash getCashAttribute(String attribute);

    /**
     * @param attribute String item saved in the {@link Entry}
     * @return value Object saved in the {@link Entry}
     */
    Object getObjectAttribute(String attribute);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

}
