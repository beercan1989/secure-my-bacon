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

import static org.hamcrest.Matchers.*;

public class PasswordEndpoint_Reading_IT extends IntegratedApiEndpoint {

    private final String endpoint = "/passwords";

    @Test // 200
    public void onFindingAllPasswords() {

        withNoAuthentication().
            baseUri(getBaseUrl()).
            get(endpoint).

            then().assertThat().

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
    public void onFindingPasswordsWithInvalidPaging() {

        // Max PerPage
        withNoAuthentication().
            queryParam("perPage", 50).
            get(endpoint).

            then().assertThat().

            statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).

            body("uuid", isA(String.class)).
            body("errors", is(not(nullValue()))).
            body("errors[0]", is(equalTo("PerPage '50' must be less than or equal to 20")));

        // Min PerPage
        withNoAuthentication().
            queryParam("perPage", -5).
            get(endpoint).

            then().assertThat().

            statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).

            body("uuid", isA(String.class)).
            body("errors", is(not(nullValue()))).
            body("errors[0]", is(equalTo("PerPage '-5' must be greater than or equal to 1")));

        // NaN PerPage
        withNoAuthentication().
            queryParam("perPage", "fred").
            get(endpoint).

            then().assertThat().

            statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).

            body("uuid", isA(String.class)).
            body("errors", is(not(nullValue()))).
            body("errors[0]", is(equalTo("Param 'perPage' requires type 'java.lang.Integer' but was provided 'fred'")));


        // Min Page
        withNoAuthentication().
            queryParam("page", -10).
            get(endpoint).

            then().assertThat().

            statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).

            body("uuid", isA(String.class)).
            body("errors", is(not(nullValue()))).
            body("errors[0]", is(equalTo("Page '-10' must be greater than or equal to 0")));

        // NaN Page
        withNoAuthentication().
            queryParam("page", "bob").
            get(endpoint).

            then().assertThat().

            statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).

            body("uuid", isA(String.class)).
            body("errors", is(not(nullValue()))).
            body("errors[0]", is(equalTo("Param 'page' requires type 'java.lang.Integer' but was provided 'bob'")));
    }
}
