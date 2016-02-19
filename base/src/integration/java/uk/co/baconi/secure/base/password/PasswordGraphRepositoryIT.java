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
import uk.co.baconi.secure.base.lock.SymmetricLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PasswordGraphRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private PasswordGraphRepository passwordGraphRepository;

    @Test
    public void shouldBeAbleToCreateAndRetrieveBag() {
        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final String passw0rd = "password";

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
    public void shouldBeAbleToSecureWithAGroup() {
        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final String passw0rd = "password";

        final Password password = new Password(whereFor, username, passw0rd);

        passwordGraphRepository.save(password);

        final Bag gitHubBag = new Bag("GitHub", "public key".getBytes());

        final SymmetricLock securedWith = new SymmetricLock(password, gitHubBag, "key".getBytes());

        assertThat(password.getSecuredBy(), is(equalTo(securedWith)));

        assertThat(gitHubBag.getId(), is(nullValue()));
        assertThat(securedWith.getId(), is(nullValue()));

        passwordGraphRepository.save(password);

        assertThat(password.getId(), is(not(nullValue())));
        assertThat(gitHubBag.getId(), is(not(nullValue())));
        assertThat(securedWith.getId(), is(not(nullValue())));
    }

}
