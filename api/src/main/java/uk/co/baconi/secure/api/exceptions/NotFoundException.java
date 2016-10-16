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

public class NotFoundException extends Exception {

    public static NotFoundException bagByName(final String name) {
        return new NotFoundException("Unable to find [Bag] by name [" + name + "]");
    }

    public static NotFoundException bagById(final long id) {
        return new NotFoundException("Unable to find [Bag] by id [" + id + "]");
    }

    public static NotFoundException userByName(final String name) {
        return new NotFoundException("Unable to find [User] by name [" + name + "]");
    }

    public static NotFoundException userById(final long id) {
        return new NotFoundException("Unable to find [User] by id [" + id + "]");
    }

    private NotFoundException(final String message) {
        super(message);
    }
}