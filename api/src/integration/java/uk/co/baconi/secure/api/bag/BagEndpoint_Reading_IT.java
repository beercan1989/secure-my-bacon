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

import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.Matchers.*;

public class BagEndpoint_Reading_IT extends IntegratedApiEndpoint {

    @Test // 200
    public void onFindingAllBags() {

        withNoAuthentication().
                baseUri(getBaseUrl()).
                get("/bags").

                then().assertThat().

                body(isJson(withJsonPath("[0].name"))).

                body("[0].name", isA(String.class)).

                statusCode(is(equalTo(HttpStatus.OK.value())));
    }

    //queryParam("page", 1).
    //queryParam("perPage", 5).
}
