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

package io.github.ghacupha.keeper.book.util;

/**
 * Exception thrown when some detail is queried from the {@link io.github.ghacupha.keeper.book.base.EntryDetails} or the
 * {@link io.github.ghacupha.keeper.book.base.AccountDetails} objects when they have not yet been set or added
 *
 * @author edwin.njeru
 */
public class UnEnteredDetailsException extends Exception {

    public UnEnteredDetailsException(String message) {
        super(message);
    }
}
