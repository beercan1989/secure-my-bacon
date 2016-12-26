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

import org.bouncycastle.util.encoders.Base64Encoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import uk.co.baconi.secure.api.exceptions.NotFoundException;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;
import uk.co.baconi.secure.api.tests.FindByUuidIntegrationTest;
import uk.co.baconi.secure.api.tests.PaginationIntegrationTest;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.cipher.asymmetric.AsymmetricCipher;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.password.Password;
import uk.co.baconi.secure.base.password.PasswordGraphRepository;
import uk.co.baconi.secure.base.user.User;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import static org.hamcrest.Matchers.*;

public class PasswordEndpoint_Reading_IT extends IntegratedApiEndpoint implements PaginationIntegrationTest, FindByUuidIntegrationTest {

    private final String endpoint = "/passwords";

    @Autowired
    private PasswordGraphRepository passwordGraphRepository;

    @Autowired
    private UserGraphRepository userGraphRepository;

    private User newUser(final String nameSpace) {
        return userGraphRepository.save(new User(nameSpace));
    }

    private Password newPassword(final String nameSpace) {
        return passwordGraphRepository.save(new Password(
                nameSpace + "_whereFor",
                nameSpace + "_username",
                (nameSpace + "_password").getBytes()
        ));
    }

    private Password newPasswordForUser(final String nameSpace) {
        final Bag bag = new Bag(nameSpace, nameSpace.getBytes());
        final User user = newUser(nameSpace);
        final Password password = newPassword(nameSpace);

        new SymmetricLock(password, bag, nameSpace.getBytes(), SymmetricCipher.AES_CBC_PKCS7);
        new AsymmetricLock(bag, user, nameSpace.getBytes(), AsymmetricCipher.RSA_ECB_PKCS1);

        return passwordGraphRepository.save(password);
    }

    @Test
    public void onFindingAllPasswords() {

        // Success
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

        // Invalid Pagination
        onEndpointWithInvalidPagingImpl(endpoint);
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
    public void onFindByUuid() {
        onFindByUuidImpl(endpoint, newPassword("onFindByUuid").getUuid());
    }

    @Test
    public void onGetPasswordsForUser() {

        final String getPasswordsForUserPath = "{base}/for-user/{user-name}";

        // Test Data
        newUser("onGetPasswordsForUser_none");
        newPasswordForUser("onGetPasswordsForUser");

        // Success with some
        withNoAuthentication().
                get(getPasswordsForUserPath, endpoint, "onGetPasswordsForUser").

                then().assertThat().

                body("data", is(not(emptyCollectionOf(String.class)))).
                body("data", hasSize(1)).
                body("data[0].whereFor", is(equalTo("onGetPasswordsForUser_whereFor"))).
                body("data[0].username", is(equalTo("onGetPasswordsForUser_username"))).
                body("data[0].password", is(equalTo(Base64Utils.encodeToString("onGetPasswordsForUser_password".getBytes())))).

                body("paging.page", isA(Integer.class)).
                body("paging.perPage", isA(Integer.class)).
                body("paging.totalCount", isA(Integer.class)).

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(1))).
                body("paging.totalCount", is(equalTo(1))).

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // Success with none
        withNoAuthentication().
                get(getPasswordsForUserPath, endpoint, "onGetPasswordsForUser_none").

                then().assertThat().

                body("data", is(emptyCollectionOf(String.class))).
                body("data", hasSize(0)).

                and().

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(0))).
                body("paging.totalCount", is(equalTo(0))).

                and().

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // no such user - empty search results not user not found
        withNoAuthentication().
                get(getPasswordsForUserPath, endpoint, "onGetPasswordsForUser_no-such-user").

                then().assertThat().

                body("data", is(emptyCollectionOf(String.class))).
                body("data", hasSize(0)).

                and().

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(0))).
                body("paging.totalCount", is(equalTo(0))).

                and().

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // invalid paging
        onEndpointWithInvalidPagingImpl(
                getPasswordsForUserPath.
                        replace("{base}", endpoint).
                        replace("{user-name}", "onGetPasswordsForUser")
        );
    }


    // onGetPasswordForUser password-uuid user-name
    // success
    // no such password
    // no such user
    // invalid uuid
}
