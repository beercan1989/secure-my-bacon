/*
 * Copyright 2016 James Bacon
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

package uk.co.baconi.secure.api.exceptions;

import java.util.UUID;

public class NotFoundException extends Exception {

    private NotFoundException(final String message) {
        super(message);
    }

    public static NotFoundException passwordByUuidForUser(final UUID uuid, final String user) {
        return new NotFoundException("Unable to find [Password] by uuid [" + uuid + "] for user [" + user + "]");
    }

    public static NotFoundException passwordByUuid(final UUID uuid) {
        return new NotFoundException("Unable to find [Password] by uuid [" + uuid + "]");
    }

    public static NotFoundException bagByName(final String name) {
        return new NotFoundException("Unable to find [Bag] by name [" + name + "]");
    }

    public static NotFoundException userByName(final String name) {
        return new NotFoundException("Unable to find [User] by name [" + name + "]");
    }
}
