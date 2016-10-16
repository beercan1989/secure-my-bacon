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
import uk.co.baconi.secure.api.integrations.RestApiAuthentication;

import static org.hamcrest.Matchers.*;

public interface FindByNameIntegrationTest extends RestApiAuthentication {

    String endpoint();

    void onFindByName();

    default void onFindByNameImpl(final String name, final int id) {

        // Found
        withNoAuthentication().
                get("{base}/by-name/{name}", endpoint(), name).

                then().assertThat().

                body("id", isA(Number.class)).
                body("name", isA(String.class)).

                body("id", is(equalTo(id))).
                body("name", is(equalTo(name))).

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // Not Found
        withNoAuthentication().
                get("{base}/by-name/{name}", endpoint(), "does-not-exist-with-this-name").

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.NOT_FOUND.value())));
    }
}
