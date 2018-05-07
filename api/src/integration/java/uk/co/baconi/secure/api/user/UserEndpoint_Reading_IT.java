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

package uk.co.baconi.secure.api.user;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;
import uk.co.baconi.secure.api.tests.FindByNameIntegrationTest;
import uk.co.baconi.secure.api.tests.PaginationIntegrationTest;

import static org.hamcrest.Matchers.*;

public class UserEndpoint_Reading_IT extends IntegratedApiEndpoint implements PaginationIntegrationTest, FindByNameIntegrationTest {

    private final String endpoint = "/users";

    @Test
    public void onFindingAllUsers() {

        // Success
        withNoAuthentication().
                get(endpoint).

                then().assertThat().

                body("data", is(not(emptyCollectionOf(String.class)))).

                body("data[0].id", is(nullValue())).
                body("data[0].name", isA(String.class)).

                body("paging.page", isA(Integer.class)).
                body("paging.perPage", isA(Integer.class)).
                body("paging.totalCount", isA(Integer.class)).

                body("data[0].name", is(equalTo("user-0"))).

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(5))).

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // Invalid Pagination
        onEndpointWithInvalidPagingImpl(endpoint);
    }

    @Test
    @Override
    public void onFindByName() {
        onFindByNameImpl(endpoint, "user-0");
    }
}
