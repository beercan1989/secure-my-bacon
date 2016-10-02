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

package uk.co.baconi.secure.api.password;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;
import uk.co.baconi.secure.api.integrations.PaginationIntegrationTest;

import static org.hamcrest.Matchers.*;

public class PasswordEndpoint_Reading_IT extends IntegratedApiEndpoint implements PaginationIntegrationTest {

    private final String endpoint = "/passwords";

    @Override
    public String endpoint() {
        return endpoint;
    }

    @Test // 200
    public void onFindingAllPasswords() {

        withNoAuthentication().
                get(endpoint).

                then().assertThat().

                body("data", is(not(emptyCollectionOf(String.class)))).

                body("data[0].whereFor", isA(String.class)).
                body("data[0].username", isA(String.class)).
                body("data[0].password", isA(String.class)).

                body("paging.page", isA(Integer.class)).
                body("paging.perPage", isA(Integer.class)).
                body("paging.totalCount", isA(Integer.class)).

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(5))).

                statusCode(is(equalTo(HttpStatus.OK.value())));
    }

    @Test // 200
    public void onFindingAllPasswordsWithPaging() {

        withNoAuthentication().
                queryParam("page", 5).
                queryParam("perPage", 5).
                get(endpoint).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.OK.value()))).

                body("paging.page", is(equalTo(5))).
                body("paging.perPage", is(equalTo(5)));

        withNoAuthentication().
                queryParam("page", 55).
                queryParam("perPage", 10).
                get(endpoint).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.OK.value()))).

                body("paging.page", is(equalTo(55))).
                body("paging.perPage", is(equalTo(10)));
    }

    @Test
    @Override
    public void onFindingWithWithInvalidPaging() {
        onFindingWithWithInvalidPagingImpl();
    }
}
