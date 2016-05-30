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

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;

import java.io.IOException;

import static org.hamcrest.Matchers.*;

public class UserEndpoint_Reading_IT extends IntegratedApiEndpoint {

    @Test // 200
    public void onFindingAllUsers() {

        withNoAuthentication().
                baseUri(getBaseUrl()).
                get("/users").

                then().assertThat().

                body("data", is(not(emptyCollectionOf(String.class)))).

                body("data[0].id", isA(Number.class)).
                body("data[0].name", isA(String.class)).

                body("paging.page", isA(Integer.class)).
                body("paging.perPage", isA(Integer.class)).
                body("paging.totalCount", isA(Integer.class)).

                body("data[0].id", is(equalTo(1))).
                body("data[0].name", is(equalTo("user-0"))).

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(5))).

                statusCode(is(equalTo(HttpStatus.OK.value())));
    }

    @Test
    public void onCreateNewUser() throws IOException {

        withNoAuthentication().
                baseUri(getBaseUrl()).
                contentType(ContentType.JSON).
                body(convertToJson(new NewUser("new-clean-user"))).
                post("/users").

                then().assertThat().

                body("id", isA(Number.class)).
                body("name", isA(String.class)).

                statusCode(is(equalTo(HttpStatus.OK.value())));
    }

    @Test
    public void onFindUserById() throws IOException {

        withNoAuthentication().
                baseUri(getBaseUrl()).
                get("/users/by-id/{id}", 1).

                then().assertThat().

                body("id", isA(Number.class)).
                body("name", isA(String.class)).

                body("id", is(equalTo(1))).
                body("name", is(equalTo("user-0"))).

                statusCode(is(equalTo(HttpStatus.OK.value())));
    }

    // Find by name

}
