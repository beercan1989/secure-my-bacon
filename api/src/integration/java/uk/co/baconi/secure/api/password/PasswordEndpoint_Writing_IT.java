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

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Headers;
import org.apache.http.Header;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import uk.co.baconi.secure.api.common.Locations;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.bag.BagGraphRepository;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static uk.co.baconi.secure.api.common.Locations.BY_UUID;
import static uk.co.baconi.secure.api.common.Locations.PASSWORDS;

public class PasswordEndpoint_Writing_IT extends IntegratedApiEndpoint {

    @Autowired
    private BagGraphRepository bagGraphRepository;

    @Test
    public void onCreateNewPassword() throws IOException {

        bagGraphRepository.save(new Bag("onCreateNewPassword-existing-bag", new byte[]{1, 2, 3}));

        withNoAuthentication().
                contentType(ContentType.JSON).
                body(convertToJson(new NewPassword(
                        new NewPassword.NewPasswordBag(
                                "onCreateNewPassword-existing-bag"
                        ),
                        new NewPassword.NewPasswordPassword(
                                "onCreateNewPassword-whereFor",
                                "onCreateNewPassword-username",
                                "onCreateNewPassword-password"
                        )
                ))).
                post(PASSWORDS).

                then().assertThat().

                body("uuid", isA(String.class)).
                body("id", is(nullValue())).
                body("whereFor", is(nullValue())).
                body("username", is(nullValue())).
                body("password", is(nullValue())).

                header("Location", startsWith(PASSWORDS + BY_UUID)).

                statusCode(is(equalTo(HttpStatus.CREATED.value())));
    }

}
