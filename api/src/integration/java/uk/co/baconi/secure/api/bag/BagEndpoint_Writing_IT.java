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

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;
import uk.co.baconi.secure.base.user.User;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import java.io.IOException;

import static org.hamcrest.Matchers.*;

public class BagEndpoint_Writing_IT extends IntegratedApiEndpoint {

    @Autowired
    private UserGraphRepository userGraphRepository;

    private final String endpoint = "/bags";

    @Test
    public void onCreateNewBag() throws IOException {

        userGraphRepository.save(new User("onCreateNewBag-existing-user"));

        withNoAuthentication().
                contentType(ContentType.JSON).
                body(convertToJson(new NewBag("new-clean-bag", "onCreateNewBag-existing-user"))).
                post(endpoint).

                then().assertThat().

                body("id", isA(Number.class)).
                body("name", isA(String.class)).
                body("name", is(equalTo("new-clean-bag"))).

                statusCode(is(equalTo(HttpStatus.OK.value())));
    }

}
