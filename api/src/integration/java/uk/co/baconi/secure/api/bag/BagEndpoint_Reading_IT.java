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

import static com.jayway.jsonpath.matchers.JsonPathMatchers.*;
import static org.hamcrest.Matchers.*;

public class BagEndpoint_Reading_IT extends IntegratedApiEndpoint {

    @Test // 200
    public void onFindingAllBags() {

        withNoAuthentication().
                baseUri(getBaseUrl()).
                get("/bags").

                then().assertThat().

                body(isJson(withJsonPath("data[0].id"))).
                body(isJson(withJsonPath("data[0].name"))).
                body(isJson(withoutJsonPath("data[0].publicKey"))).

                body(isJson(withJsonPath("paging.page"))).
                body(isJson(withJsonPath("paging.perPage"))).
                body(isJson(withJsonPath("paging.totalCount"))).

                body("data[0].id", isA(Integer.class)).
                body("data[0].name", isA(String.class)).

                body("paging.page", isA(Integer.class)).
                body("paging.perPage", isA(Integer.class)).
                body("paging.totalCount", isA(Integer.class)).

                body("paging.page", is(equalTo(1))).
                body("paging.perPage", is(equalTo(5))).

                statusCode(is(equalTo(HttpStatus.OK.value())));
    }

    //queryParam("page", 1).
    //queryParam("perPage", 5).
}
