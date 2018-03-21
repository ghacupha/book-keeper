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

package io.github.ghacupha.keeper.book.balance;

import io.github.ghacupha.keeper.book.api.Account;
import io.github.ghacupha.keeper.book.api.Entry;

/**
 * Denotes the side to which a transaction belongs on the balance sheet. Either the debit side or credit side
 * of the Journal entry. It also show the "default" that an {@link Account} belongs to before any {@link Entry}
 * items are posted
 *
 * @author edwin.njeru
 */
public enum JournalSide {

    /**
     * Debit side of the balance sheet
     */
    DEBIT {
        public String toString() {
            return "DR";
        }
    },

    /**
     * Credit side of the balance sheet
     */
    CREDIT {
        public String toString() {
            return "CR";
        }
    }
}
