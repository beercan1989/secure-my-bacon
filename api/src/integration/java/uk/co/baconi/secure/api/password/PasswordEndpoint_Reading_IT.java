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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import uk.co.baconi.secure.api.exceptions.NotFoundException;
import uk.co.baconi.secure.api.integrations.IntegratedApiEndpoint;
import uk.co.baconi.secure.api.tests.FindByUuidIntegrationTest;
import uk.co.baconi.secure.api.tests.PaginationIntegrationTest;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.password.EncryptedPassword;
import uk.co.baconi.secure.base.password.PasswordGraphRepository;
import uk.co.baconi.secure.base.password.PasswordService;
import uk.co.baconi.secure.base.user.User;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static uk.co.baconi.secure.api.common.Locations.PASSWORDS;
import static uk.co.baconi.secure.base.cipher.asymmetric.AsymmetricCipher.RSA_ECB_PKCS1;
import static uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher.AES_CBC_PKCS7;

public class PasswordEndpoint_Reading_IT extends IntegratedApiEndpoint implements PaginationIntegrationTest, FindByUuidIntegrationTest {

    @Autowired
    private PasswordGraphRepository passwordGraphRepository;

    @Autowired
    private UserGraphRepository userGraphRepository;

    @Autowired
    private PasswordService passwordService;

    private User newUser(final String nameSpace) {
        return userGraphRepository.save(new User(nameSpace));
    }

    private EncryptedPassword newPassword(final Bag bag, final String nameSpace) throws EncryptionException {
            return passwordService.createAndShare(
                    bag,
                    nameSpace + "_whereFor",
                    nameSpace + "_username",
                    nameSpace + "_password",
                    AES_CBC_PKCS7,
                    128
            );
    }

    private EncryptedPassword newPasswordForUser(final String nameSpace) throws EncryptionException {
        final Bag bag = new Bag(nameSpace, nameSpace.getBytes());
        final User user = newUser(nameSpace);
        final EncryptedPassword password = newPassword(bag, nameSpace);

        new SymmetricLock(password, bag, nameSpace.getBytes(), AES_CBC_PKCS7);
        new AsymmetricLock(bag, user, nameSpace.getBytes(), RSA_ECB_PKCS1);

        return passwordGraphRepository.save(password);
    }

    @Test
    public void onFindingAllPasswords() {

        // Success
        withNoAuthentication().
                get(PASSWORDS).

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
        onEndpointWithInvalidPagingImpl(PASSWORDS);
    }

    @Test // 200
    public void onFindingAllPasswordsWithPaging() {

        withNoAuthentication().
                queryParam("page", 5).
                queryParam("perPage", 5).
                get(PASSWORDS).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.OK.value()))).

                body("paging.page", is(equalTo(5))).
                body("paging.perPage", is(equalTo(5)));

        withNoAuthentication().
                queryParam("page", 55).
                queryParam("perPage", 10).
                get(PASSWORDS).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.OK.value()))).

                body("paging.page", is(equalTo(55))).
                body("paging.perPage", is(equalTo(10)));
    }

    @Test
    @Override
    public void onFindByUuid() throws EncryptionException {
        onFindByUuidImpl(PASSWORDS, newPasswordForUser("onFindByUuid").getUuid());
    }

    @Test
    public void onGetPasswordsForUser() throws EncryptionException {

        final String getPasswordsForUserPath = "{base}/for-user/{user-name}";

        // Test Data
        newUser("onGetPasswordsForUser_none");
        newPasswordForUser("onGetPasswordsForUser");

        // Success with one
        withNoAuthentication().
                get(getPasswordsForUserPath, PASSWORDS, "onGetPasswordsForUser").

                then().assertThat().

                body("data", is(not(emptyCollectionOf(String.class)))).
                body("data", hasSize(1)).
                body("data[0].whereFor", is(equalTo("onGetPasswordsForUser_whereFor"))).
                body("data[0].username", is(equalTo("onGetPasswordsForUser_username"))).
                body("data[0].password", is(not(equalTo(Base64Utils.encodeToString("onGetPasswordsForUser_password".getBytes()))))).

                body("paging.page", isA(Integer.class)).
                body("paging.perPage", isA(Integer.class)).
                body("paging.totalCount", isA(Integer.class)).

                body("paging.page", is(equalTo(0))).
                body("paging.perPage", is(equalTo(1))).
                body("paging.totalCount", is(equalTo(1))).

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // Success with none
        withNoAuthentication().
                get(getPasswordsForUserPath, PASSWORDS, "onGetPasswordsForUser_none").

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
                get(getPasswordsForUserPath, PASSWORDS, "onGetPasswordsForUser_no-such-user").

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
                        replace("{base}", PASSWORDS).
                        replace("{user-name}", "onGetPasswordsForUser")
        );
    }


    // onGetPasswordForUser password-uuid user-name
    @Test
    public void onGetPasswordForUser() throws EncryptionException {

        final String getPasswordForUserPath = "{base}/by-uuid/{password-uuid}/for-user/{user-name}";

        // Test Data
        final User userWithNoPasswords = newUser("onGetPasswordForUser_none");
        final EncryptedPassword passwordForUserWithPasswords = newPasswordForUser("onGetPasswordForUser");

        // Success with one
        withNoAuthentication().
                get(getPasswordForUserPath, PASSWORDS, passwordForUserWithPasswords.getUuid(), "onGetPasswordForUser").

                then().assertThat().

                body("uuid", is(equalTo(passwordForUserWithPasswords.getUuid().toString()))).
                body("whereFor", is(equalTo(passwordForUserWithPasswords.getWhereFor()))).
                body("username", is(equalTo(passwordForUserWithPasswords.getUsername()))).
                body("password", is(equalTo(Base64Utils.encodeToString(passwordForUserWithPasswords.getPassword())))).

                body("id", is(nullValue())).
                body("securedBy", is(nullValue())).

                statusCode(is(equalTo(HttpStatus.OK.value())));

        // Fail because of no such link to password
        withNoAuthentication().
                get(getPasswordForUserPath, PASSWORDS, passwordForUserWithPasswords.getUuid(), userWithNoPasswords.getName()).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.NOT_FOUND.value()))).

                and().

                body("uuid", isA(String.class)).
                body("name", isA(String.class)).
                body("name", is(equalTo(NotFoundException.class.getName())));

        // Fail because of no such password
        withNoAuthentication().
                get(getPasswordForUserPath, PASSWORDS, UUID.randomUUID(), userWithNoPasswords.getName()).

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.NOT_FOUND.value()))).

                and().

                body("uuid", isA(String.class)).
                body("name", isA(String.class)).
                body("name", is(equalTo(NotFoundException.class.getName())));

        // Fail because of no such user
        withNoAuthentication().
                get(getPasswordForUserPath, PASSWORDS, passwordForUserWithPasswords.getUuid(), "onGetPasswordForUser_does-not-exist").

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.NOT_FOUND.value()))).

                and().

                body("uuid", isA(String.class)).
                body("name", isA(String.class)).
                body("name", is(equalTo(NotFoundException.class.getName())));

        // Fail because of invalid password UUID
        withNoAuthentication().
                get(getPasswordForUserPath, PASSWORDS, "invalid-uuid-string", "onGetPasswordForUser_none").

                then().assertThat().

                statusCode(is(equalTo(HttpStatus.BAD_REQUEST.value()))).

                and().

                body("uuid", isA(String.class)).
                body("errors", isA(Collection.class)).
                body("errors[0]", containsString(
                        "requires type 'java.util.UUID' but was provided 'invalid-uuid-string'"
                ));
    }
}
