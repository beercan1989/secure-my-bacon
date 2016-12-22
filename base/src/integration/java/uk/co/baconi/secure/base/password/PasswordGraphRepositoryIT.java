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

package uk.co.baconi.secure.base.password;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.baconi.secure.base.BaseIntegrationTest;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.user.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PasswordGraphRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private PasswordGraphRepository passwordGraphRepository;

    @Test
    public void shouldBeAbleToCreateAndRetrievePassword() {
        final String whereFor = "https://github.com/login";
        final String username = "shouldBeAbleToCreateAndRetrievePassword";
        final byte[] passw0rd = "password".getBytes();

        final Password password = new Password(whereFor, username, passw0rd);

        final Password saved = passwordGraphRepository.save(password);

        assertThat(saved, is(equalTo(password)));
        assertThat(saved.getId(), is(not(nullValue())));
        assertThat(saved.getWhereFor(), is(equalTo(whereFor)));
        assertThat(saved.getUsername(), is(equalTo(username)));
        assertThat(saved.getPassword(), is(equalTo(passw0rd)));
        assertThat(saved.getSecuredBy(), is(nullValue()));

        final Password one = passwordGraphRepository.findOne(password.getId());

        assertThat(one, is(equalTo(saved)));
        assertThat(one.getId(), is(equalTo(password.getId())));
        assertThat(one.getWhereFor(), is(equalTo(whereFor)));
        assertThat(one.getUsername(), is(equalTo(username)));
        assertThat(one.getPassword(), is(equalTo(passw0rd)));
        assertThat(one.getSecuredBy(), is(nullValue()));
    }

    @Test
    public void shouldBeAbleToSecurePasswordWithAGroup() {
        final String whereFor = "https://github.com/login";
        final String username = "shouldBeAbleToSecurePasswordWithAGroup";
        final String passw0rd = "password";

        final Password password = new Password(whereFor, username, passw0rd.getBytes());

        passwordGraphRepository.save(password);

        final Bag gitHubBag = new Bag("GitHub", "public key".getBytes());

        final SymmetricLock securedWith = new SymmetricLock(password, gitHubBag, "key".getBytes(), SymmetricCipher.AES_CBC_PKCS7);

        assertThat(password.getSecuredBy(), is(equalTo(securedWith)));

        assertThat(gitHubBag.getId(), is(nullValue()));
        assertThat(securedWith.getId(), is(nullValue()));

        passwordGraphRepository.save(password);

        assertThat(password.getId(), is(not(nullValue())));
        assertThat(gitHubBag.getId(), is(not(nullValue())));
        assertThat(securedWith.getId(), is(not(nullValue())));
    }

    @Test
    public void shouldBeAbleToFindPasswordsByUser() {

        //
        // Test Data Setup
        //
        final Password password_1 = passwordGraphRepository.save(new Password(
                "shouldBeAbleToFindPasswordsByUser_whereFor_1",
                "shouldBeAbleToFindPasswordsByUser_username_1",
                "shouldBeAbleToFindPasswordsByUser_password_1".getBytes()
        ));

        final Password password_2 = passwordGraphRepository.save(new Password(
                "shouldBeAbleToFindPasswordsByUser_whereFor_2",
                "shouldBeAbleToFindPasswordsByUser_username_2",
                "shouldBeAbleToFindPasswordsByUser_password_2".getBytes()
        ));

        final Password password_3 = passwordGraphRepository.save(new Password(
                "shouldBeAbleToFindPasswordsByUser_whereFor_3",
                "shouldBeAbleToFindPasswordsByUser_username_3",
                "shouldBeAbleToFindPasswordsByUser_password_3".getBytes()
        ));

        final Bag bag = new Bag("shouldBeAbleToFindPasswordsByUser_group", "public key".getBytes());

        new SymmetricLock(password_1, bag, "key_1".getBytes(), SymmetricCipher.AES_CBC_PKCS7);
        new SymmetricLock(password_2, bag, "key_2".getBytes(), SymmetricCipher.AES_CBC_PKCS7);
        new SymmetricLock(password_3, bag, "key_3".getBytes(), SymmetricCipher.AES_CBC_PKCS7);

        final User user = new User("shouldBeAbleToFindPasswordsByUser_username");

        new AsymmetricLock(bag, user, "private key".getBytes());

        passwordGraphRepository.save(password_1);
        passwordGraphRepository.save(password_2);
        passwordGraphRepository.save(password_3);

        //
        // Actual Test
        //
        final List<Password> passwordsByUser = passwordGraphRepository.getPasswordsByUser(user.getName());
        assertThat(passwordsByUser, is(not(nullValue())));
        assertThat(passwordsByUser, containsInAnyOrder(password_1, password_2, password_3));
    }

    @Test
    public void shouldBeAbleToFindPasswordByUser() {

        //
        // Test Data Setup
        //

        final Password password = new Password(
                "shouldBeAbleToFindPasswordByUser_whereFor",
                "shouldBeAbleToFindPasswordByUser_username",
                "shouldBeAbleToFindPasswordByUser_password".getBytes()
        );

        final Bag bag = new Bag("shouldBeAbleToFindPasswordByUser_group", "public key".getBytes());

        new SymmetricLock(password, bag, "key".getBytes(), SymmetricCipher.AES_CBC_PKCS7);

        final User user = new User("shouldBeAbleToFindPasswordByUser_username");

        new AsymmetricLock(bag, user, "private key".getBytes());

        passwordGraphRepository.save(password);

        //
        // Actual Test
        //
        final Password passwordByUser = passwordGraphRepository.getPasswordByUser(password.getId(), user.getName());
        assertThat(passwordByUser, is(not(nullValue())));
        assertThat(passwordByUser.getId(), is(equalTo(password.getId())));
        assertThat(passwordByUser, is(equalTo(password)));
    }
}
