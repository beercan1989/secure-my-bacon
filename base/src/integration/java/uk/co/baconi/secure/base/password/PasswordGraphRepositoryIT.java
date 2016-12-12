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
    public void shouldBeAbleToFindPasswordByUser() {

        //
        // Test Data Setup
        //

        final String whereFor = "shouldBeAbleToFindPasswordByUser_whereFor";
        final String username = "shouldBeAbleToFindPasswordByUser_username";
        final String passw0rd = "shouldBeAbleToFindPasswordByUser_password";

        final Password password = new Password(whereFor, username, passw0rd.getBytes());

        passwordGraphRepository.save(password);

        final Bag bag = new Bag("shouldBeAbleToFindPasswordByUser_group", "public key".getBytes());

        final SymmetricLock securedWith = new SymmetricLock(password, bag, "key".getBytes(), SymmetricCipher.AES_CBC_PKCS7);

        assertThat(password.getSecuredBy(), is(equalTo(securedWith)));

        final User user = new User("shouldBeAbleToFindPasswordByUser_username");

        final AsymmetricLock sharedWith = new AsymmetricLock(bag, user, "private key".getBytes());

        assertThat(bag.getShared(), contains(sharedWith));

        assertThat(bag.getId(), is(nullValue()));
        assertThat(securedWith.getId(), is(nullValue()));
        assertThat(user.getId(), is(nullValue()));
        assertThat(sharedWith.getId(), is(nullValue()));

        passwordGraphRepository.save(password);

        assertThat(password.getId(), is(not(nullValue())));
        assertThat(bag.getId(), is(not(nullValue())));
        assertThat(securedWith.getId(), is(not(nullValue())));
        assertThat(user.getId(), is(not(nullValue())));
        assertThat(sharedWith.getId(), is(not(nullValue())));

        //
        // Actual Test
        //
        final Password passwordByUser = passwordGraphRepository.getPasswordByUser(password.getId(), user.getId());
        assertThat(passwordByUser.getId(), is(equalTo(password.getId())));
        assertThat(passwordByUser, is(equalTo(password)));
    }
}
