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

package io.github.ghacupha.keeper.book.internal;

import io.github.ghacupha.keeper.book.Entry;
import io.github.ghacupha.keeper.book.EntryAttributes;
import io.github.ghacupha.keeper.book.unit.money.Cash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container for additional details for {@link Entry}
 *
 * @author edwin.njeru
 */
public class EntryDetails implements EntryAttributes {

    private final Map<String, String> stringMap = new ConcurrentHashMap<>();
    private final Map<String, Cash> cashMap = new ConcurrentHashMap<>();
    private final Map<String, Object> objectMap = new ConcurrentHashMap<>();

    /**
     * @param narration Explanation for the {@link Entry}
     */
    public EntryDetails(String narration) {
        stringMap.put("narration", narration);
        stringMap.put("remarks", "");
        stringMap.put("referenceNumber", "");
        stringMap.put("party", "");
    }

    /**
     * @param narration       Explanation for the {@link Entry}
     * @param referenceNumber Unique id of the entry which could be an invoice number
     *                        debit note,credit note etc
     */
    public EntryDetails(String narration, String referenceNumber) {
        this(narration);
        stringMap.put("referenceNumber", referenceNumber);
        stringMap.put("remarks", "");
        stringMap.put("party", "");
    }

    /**
     * @param narration       Explanation for the {@link Entry}
     * @param referenceNumber Unique id of the entry which could be an invoice number
     *                        debit note,credit note etc
     * @param remarks         Additional processing remarks in regards to the entry
     */
    public EntryDetails(String narration, String referenceNumber, String remarks) {
        this(narration, referenceNumber);
        stringMap.put("remarks", remarks);
        stringMap.put("party", "");
    }

    /**
     * @param narration       Explanation for the {@link Entry}
     * @param referenceNumber Unique id of the entry which could be an invoice number
     *                        debit note,credit note etc
     * @param remarks         Additional processing remarks in regards to the entry
     * @param party           With whom the business is transacting as far as the entry is concerned
     */
    public EntryDetails(String narration, String referenceNumber, String remarks, String party) {
        this(narration, referenceNumber, remarks);
        stringMap.put("party", party);
    }

    public static EntryAttributes newDetails(String narration, String referenceNumber) {

        return new EntryDetails(narration, referenceNumber);
    }

    public static EntryAttributes newDetails(String narration, String referenceNumber, String remarks) {

        return new EntryDetails(narration, referenceNumber, remarks);
    }

    public static EntryAttributes newDetails(String narration, String referenceNumber, String remarks, String party) {

        return new EntryDetails(narration, referenceNumber, remarks, party);
    }

    /**
     * @return Narration for the entry made in the account
     */
    @Override
    public String getNarration() {
        return stringMap.get("narration");
    }

    /**
     * @return Additional remarks made in the account iif any
     */
    @Override
    public String getRemarks() {
        return stringMap.get("remarks");
    }

    /**
     * @return referenceNumber in regard to the entry, could be debit note number or invoice
     * number
     */
    @Override
    public String getReferenceNumber() {
        return stringMap.get("referenceNumber");
    }

    /**
     * @return Name of the party with whom the concern is conducting business
     */
    @Override
    public String getParty() {
        return stringMap.get("party");
    }

    /**
     * @param attribute Attribute name as String
     * @param value     Value for the attribute as String
     */
    @Override
    public void setStringAttribute(String attribute, String value) {

        stringMap.put(attribute, value);
    }

    /**
     * @param cashAttribute Attribute name as String
     * @param value         Value for the attribute as {@link Cash}
     */
    @Override
    public void setCashAttribute(String cashAttribute, Cash value) {

        cashMap.put(cashAttribute, value);
    }

    /**
     * @param attribute String item saved in the {@link Entry}
     * @return String item saved in the {@link Entry}
     */
    @Override
    public String getStringAttribute(String attribute) {

        return stringMap.get(attribute);
    }

    /**
     * @param attribute String item saved in the {@link Entry}
     * @return {@link Cash} item saved in the {@link Entry}
     */
    @Override
    public Cash getCashAttribute(String attribute) {

        return cashMap.get(attribute);
    }

    /**
     * @param attribute String item saved in the {@link Entry}
     * @return value Object saved in the {@link Entry}
     */
    @Override
    public Object getObjectAttribute(String attribute) {

        return objectMap.get(attribute);
    }

    /**
     * @param objectAttribute Attribute name as String
     * @param value           Value for the attribute as any {@link Object}
     */
    @Override
    public void setObjectAttribute(String objectAttribute, Object value) {

        objectMap.put(objectAttribute, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntryDetails that = (EntryDetails) o;

        return stringMap.equals(that.stringMap) && cashMap.equals(that.cashMap) && objectMap.equals(that.objectMap);
    }

    @Override
    public int hashCode() {
        int result = stringMap.hashCode();
        result = 31 * result + cashMap.hashCode();
        result = 31 * result + objectMap.hashCode();
        return result;
    }

    @SuppressWarnings("all")
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntryDetails{");
        sb.append("narration='").append(stringMap.get("narration")).append('\'');
        sb.append(", remarks='").append(stringMap.get("remarks")).append('\'');
        sb.append(", invoiceNumber='").append(stringMap.get("invoiceNumber")).append('\'');
        sb.append(", supplier='").append(stringMap.get("supplier")).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
