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

package uk.co.baconi.secure.password;

import org.junit.Test;
import uk.co.baconi.secure.lock.SymmetricLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class PasswordTest {

    @Test
    public void shouldBeAbleToReadProperties() {

        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final String passw0rd = "password";

        final Password password = new Password(whereFor, username, passw0rd);

        assertThat(password.getId(), is(nullValue()));
        assertThat(password.getWhereFor(), is(equalTo(whereFor)));
        assertThat(password.getUsername(), is(equalTo(username)));
        assertThat(password.getPassword(), is(equalTo(passw0rd)));
        assertThat(password.getSecuredBy(), is(nullValue()));
    }

    @Test
    public void shouldBeAbleToChangeProperties() {

        final SymmetricLock symmetricLock = mock(SymmetricLock.class);
        final Password password = new Password("", "", "").securedBy(symmetricLock);
        assertThat(password.getSecuredBy(), is(equalTo(symmetricLock)));

        final String newWhereFor = "https://bitbucket.org/account/signin/?next=/";
        password.setWhereFor(newWhereFor);
        assertThat(password.getWhereFor(), is(equalTo(newWhereFor)));

        final String newUsername = "substepsTeam";
        password.setUsername(newUsername);
        assertThat(password.getUsername(), is(equalTo(newUsername)));

        final String newPassw0rd = "P@55w0rd!";
        password.setPassword(newPassw0rd);
        assertThat(password.getPassword(), is(equalTo(newPassw0rd)));

        final SymmetricLock newSymmetricLock = mock(SymmetricLock.class);
        password.securedBy(newSymmetricLock);
        assertThat(password.getSecuredBy(), is(equalTo(newSymmetricLock)));
    }

    @Test
    public void shouldImplementEqualsCorrectly() {

        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final String passw0rd = "password";

        final Password password1 = new Password(whereFor, username, passw0rd);
        final Password password2 = new Password(whereFor, username, passw0rd);

        assertThat(password1, is(equalTo(password2)));
        assertThat(password1.hashCode(), is(equalTo(password2.hashCode())));

        final Password password3 = new Password(whereFor, "beercan", passw0rd);

        assertThat(password1, is(not(equalTo(password3))));
        assertThat(password2, is(not(equalTo(password3))));
        assertThat(password1.hashCode(), is(not(equalTo(password3.hashCode()))));
        assertThat(password2.hashCode(), is(not(equalTo(password3.hashCode()))));

        final Password password4 = new Password(whereFor, username, "P@55w0rd!");

        assertThat(password1, is(not(equalTo(password4))));
        assertThat(password2, is(not(equalTo(password4))));
        assertThat(password3, is(not(equalTo(password4))));
        assertThat(password1.hashCode(), is(not(equalTo(password4.hashCode()))));
        assertThat(password2.hashCode(), is(not(equalTo(password4.hashCode()))));
        assertThat(password3.hashCode(), is(not(equalTo(password4.hashCode()))));

        final Password password5 = new Password("https://bitbucket.org/account/signin/?next=/", username, passw0rd);

        assertThat(password1, is(not(equalTo(password5))));
        assertThat(password2, is(not(equalTo(password5))));
        assertThat(password3, is(not(equalTo(password5))));
        assertThat(password4, is(not(equalTo(password5))));
        assertThat(password1.hashCode(), is(not(equalTo(password5.hashCode()))));
        assertThat(password2.hashCode(), is(not(equalTo(password5.hashCode()))));
        assertThat(password3.hashCode(), is(not(equalTo(password5.hashCode()))));
        assertThat(password4.hashCode(), is(not(equalTo(password5.hashCode()))));

        final SymmetricLock asymmetricLock = mock(SymmetricLock.class);

        password1.securedBy(asymmetricLock);

        assertThat(password1, is(equalTo(password2)));
        assertThat(password1.hashCode(), is(equalTo(password2.hashCode())));
    }

    @Test
    public void shouldHaveNiceToStringRepresentation() {
        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final String passw0rd = "p@55w0rd!";

        final Password password = new Password(whereFor, username, passw0rd);

        final String passwordAsString = password.toString();

        assertThat(passwordAsString, containsString("id=null,"));
        assertThat(passwordAsString, containsString("whereFor='https://github.com/login',"));
        assertThat(passwordAsString, containsString("username='beercan1989',"));
        assertThat(passwordAsString, containsString("password='p@55w0rd!'"));
    }
}