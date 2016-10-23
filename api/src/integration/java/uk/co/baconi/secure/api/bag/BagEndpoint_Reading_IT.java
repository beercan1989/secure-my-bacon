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

package uk.co.baconi.secure.api.bag;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;
import uk.co.baconi.secure.api.tests.FindByIdIntegrationTest;
import uk.co.baconi.secure.api.tests.FindByNameIntegrationTest;
import uk.co.baconi.secure.api.tests.PaginationIntegrationTest;

import static org.hamcrest.Matchers.*;

public class BagEndpoint_Reading_IT extends IntegratedApiEndpoint implements PaginationIntegrationTest, FindByIdIntegrationTest, FindByNameIntegrationTest {

    private final String endpoint = "/bags";

    @Override
    public String endpoint() {
        return endpoint;
    }

    @Test // 200
    public void onFindingAllBags() {

        withNoAuthentication().
                get(endpoint).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.OK.value()))).

                body("data", is(not(emptyCollectionOf(String.class)))).

                body("data[0].id", isA(Integer.class)).
                body("data[0].name", isA(String.class)).

                body("data[1].id", isA(Integer.class)).
                body("data[1].name", isA(String.class)).

                body("data[2].id", isA(Integer.class)).
                body("data[2].name", isA(String.class)).

                body("data[3].id", isA(Integer.class)).
                body("data[3].name", isA(String.class)).

                body("data[4].id", isA(Integer.class)).
                body("data[4].name", isA(String.class)).

                body("paging.page", isA(Integer.class)).
                body("paging.perPage", isA(Integer.class)).
                body("paging.totalCount", isA(Integer.class)).

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(5)));
    }

    @Test // 200
    public void onFindingAllBagsWithPaging() {

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
    public void onFindById() {
        onFindByIdImpl(0, "bag-0");
    }

    @Test
    @Override
    public void onFindByName() {
        onFindByNameImpl("bag-0", 0);
    }

    @Test
    @Override
    public void onFindingWithWithInvalidPaging() {
        onFindingWithWithInvalidPagingImpl();
    }
}
