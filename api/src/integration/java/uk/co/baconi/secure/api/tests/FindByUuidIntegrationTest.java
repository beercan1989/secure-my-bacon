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

package uk.co.baconi.secure.api.tests;

import org.springframework.http.HttpStatus;
import uk.co.baconi.secure.api.exceptions.NotFoundException;
import uk.co.baconi.secure.api.integrations.RestApiAuthentication;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

public interface FindByUuidIntegrationTest extends RestApiAuthentication {

    String FIND_BY_UUID_PATH = "{base}/by-uuid/{uuid}";

    String endpoint();

    void onFindByUuid();

    default void onFindByUuidImpl(final UUID uuid) {

        // Found
        withNoAuthentication().
                get(FIND_BY_UUID_PATH, endpoint(), uuid).

                then().assertThat().

                body("uuid", isA(String.class)).
                body("uuid", is(equalTo(uuid.toString()))).

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // Not Found
        withNoAuthentication().
                get(FIND_BY_UUID_PATH, endpoint(), UUID.randomUUID()).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.NOT_FOUND.value()))).

                and().

                body("uuid", isA(String.class)).
                body("name", isA(String.class)).
                body("name", is(equalTo(NotFoundException.class.getName())));

        // Invalid UUID
        withNoAuthentication().
                get(FIND_BY_UUID_PATH, endpoint(), "invalid-uuid-string").

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).

                and().

                body("uuid", isA(String.class)).
                body("errors", isA(Collection.class)).
                body("errors[0]", containsString(
                        "requires type 'java.util.UUID' but was provided 'invalid-uuid-string'"
                ));
    }
}
