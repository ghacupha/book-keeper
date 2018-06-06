/*
 * Copyright © 2018 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.ghacupha.keeper.book.util.UnEnteredDetailsException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class EntryDetails {

    private final String narration;

    private final Map<String,Object> entryMap = new ConcurrentHashMap<>();

    public EntryDetails(String narration) {
        this.narration = narration;
    }

    public String getNarration() {
        return narration;
    }

    public Map<String, Object> getEntryMap() {
        return entryMap;
    }

    public void setAttribute(String label, Object attribute){
        entryMap.put(label,attribute);
    }

    public Object getAttribute(String label) throws UnEnteredDetailsException {

        if(!entryMap.containsKey(label)){
            throw new UnEnteredDetailsException(String.format("Could not find %s since it was never added in the first place",label));
        } else {
            return entryMap.get(label);
        }
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
        return Objects.equals(narration, that.narration) && Objects.equals(entryMap, that.entryMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(narration, entryMap);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("'").append(narration).append('\'');
        sb.append(", otherEntryDetails=").append(entryMap);
        sb.append('}');
        return sb.toString();
    }
}
