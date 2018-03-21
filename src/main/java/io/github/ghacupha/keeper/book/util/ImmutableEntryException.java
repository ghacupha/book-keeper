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

import io.github.ghacupha.keeper.book.api.Entry;
import io.github.ghacupha.keeper.book.api.EntryAttributes;

/**
 * Exception thrown when {@link EntryAttributes} are added to an {@link Entry} after the {@link Entry} has been set
 *
 * @author edwin.njeru
 */
public class ImmutableEntryException extends Throwable {

    private static final long serialVersionUID = 3871181286857327591L;

    public ImmutableEntryException(String message) {
        super(message);
    }

    public ImmutableEntryException() {
        super();
    }
}
