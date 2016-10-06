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

package uk.co.baconi.secure.api.integrations;

import com.jayway.restassured.response.ValidatableResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.Matchers.*;

public interface PaginationIntegrationTest extends Authentication {

    String endpoint();

    void onFindingWithWithInvalidPaging();

    default void onFindingWithWithInvalidPagingImpl() {

        final BiFunction<Object, Object, ValidatableResponse> perPageCheck = (perPage, error) -> withNoAuthentication().
                queryParam("perPage", perPage).
                get(endpoint()).
                then().assertThat().
                statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).
                body("uuid", isA(String.class)).
                body("errors", is(not(nullValue()))).
                body("errors", isA((List.class))).
                body("errors", is(contains(error))).
                body("errors[0]", is(equalTo(error)));

        // Max PerPage
        perPageCheck.apply(50, "PerPage '50' must be less than or equal to 20");

        // Min PerPage
        perPageCheck.apply(-5, "PerPage '-5' must be greater than or equal to 1");

        // NaN PerPage
        perPageCheck.apply("fred", "Param 'perPage' requires type 'java.lang.Integer' but was provided 'fred'");

        final BiFunction<Object, Object, ValidatableResponse> pageCheck = (page, error) -> withNoAuthentication().
                queryParam("page", page).
                get(endpoint()).
                then().assertThat().
                statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).
                body("uuid", isA(String.class)).
                body("errors", is(not(nullValue()))).
                body("errors", isA((List.class))).
                body("errors", is(contains(error))).
                body("errors[0]", is(equalTo(error)));

        // Min Page
        pageCheck.apply(-10, "Page '-10' must be greater than or equal to 0");

        // NaN Page
        pageCheck.apply("bob", "Param 'page' requires type 'java.lang.Integer' but was provided 'bob'");
    }
}
