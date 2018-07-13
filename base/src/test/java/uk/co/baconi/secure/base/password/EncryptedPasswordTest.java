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
import uk.co.baconi.secure.base.BaseUnitTest;
import uk.co.baconi.secure.base.lock.SymmetricLock;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class EncryptedPasswordTest extends BaseUnitTest {

    @Test
    public void shouldBeAbleToReadProperties() {

        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final byte[] passw0rd = "password".getBytes();

        final EncryptedPassword password = new EncryptedPassword(whereFor, username, passw0rd);

        assertThat(password.getWhereFor(), is(equalTo(whereFor)));
        assertThat(password.getUsername(), is(equalTo(username)));
        assertThat(password.getPassword(), is(equalTo(passw0rd)));
        assertThat(password.getSecuredBy(), is(nullValue()));
    }

    @Test
    public void shouldBeAbleToChangeProperties() {

        final SymmetricLock symmetricLock = mock(SymmetricLock.class);
        final EncryptedPassword password = new EncryptedPassword("", "", "".getBytes()).securedBy(symmetricLock);
        assertThat(password.getSecuredBy(), is(equalTo(symmetricLock)));

        final String newWhereFor = "https://bitbucket.org/account/signin/?next=/";
        password.setWhereFor(newWhereFor);
        assertThat(password.getWhereFor(), is(equalTo(newWhereFor)));

        final String newUsername = "substepsTeam";
        password.setUsername(newUsername);
        assertThat(password.getUsername(), is(equalTo(newUsername)));

        final byte[] newPassw0rd = "P@55w0rd!".getBytes();
        password.setPassword(newPassw0rd);
        assertThat(password.getPassword(), is(equalTo(newPassw0rd)));

        final SymmetricLock newSymmetricLock = mock(SymmetricLock.class);
        password.securedBy(newSymmetricLock);
        assertThat(password.getSecuredBy(), is(equalTo(newSymmetricLock)));
    }

    @Test
    public void shouldImplementEqualsCorrectly() {

        final UUID uuidOne = UUID.randomUUID();
        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final byte[] passw0rd = "password".getBytes();

        final EncryptedPassword password1 = new EncryptedPassword(uuidOne, whereFor, username, passw0rd);
        final EncryptedPassword password2 = new EncryptedPassword(uuidOne, whereFor, username, passw0rd);

        assertThat(password1, is(equalTo(password2)));
        assertThat(password1.hashCode(), is(equalTo(password2.hashCode())));

        password1.securedBy(mock(SymmetricLock.class));
        assertThat(password1, is(equalTo(password2)));
        assertThat(password1.hashCode(), is(equalTo(password2.hashCode())));

        final EncryptedPassword password3 = new EncryptedPassword(UUID.randomUUID(), whereFor, username, passw0rd);
        assertThat(password1, is(not(equalTo(password3))));
        assertThat(password1.hashCode(), is(not(equalTo(password3.hashCode()))));

        final EncryptedPassword password4 = new EncryptedPassword(uuidOne, whereFor, username, "P@55w0rd!".getBytes());
        assertThat(password1, is(equalTo(password4)));
        assertThat(password1.hashCode(), is(equalTo(password4.hashCode())));

        final EncryptedPassword password5 = new EncryptedPassword(uuidOne, "https://bitbucket.org/account/signin/?next=/", username, passw0rd);
        assertThat(password1, is(equalTo(password5)));
        assertThat(password1.hashCode(), is(equalTo(password5.hashCode())));

        final EncryptedPassword password6 = new EncryptedPassword(uuidOne, whereFor, "beercan", passw0rd);
        assertThat(password1, is(equalTo(password6)));
        assertThat(password1.hashCode(), is(equalTo(password6.hashCode())));
    }

    @Test
    public void shouldHaveNiceToStringRepresentation() {
        final UUID uuid = UUID.fromString("ee145c66-8cfb-11e7-bb31-be2e44b06b34");
        final String whereFor = "https://github.com/login";
        final String username = "beercan1989";
        final String passw0rd = "p@55w0rd!";

        final EncryptedPassword password = new EncryptedPassword(uuid, whereFor, username, passw0rd.getBytes());

        final String passwordAsString = password.toString();

        assertThat(passwordAsString, containsString("id=null,"));
        assertThat(passwordAsString, containsString("uuid=ee145c66-8cfb-11e7-bb31-be2e44b06b34,"));
        assertThat(passwordAsString, containsString("whereFor=https://github.com/login,"));
        assertThat(passwordAsString, not(containsString("password=")));
        assertThat(passwordAsString, containsString("username=beercan1989)"));
    }

    @Test
    public void shouldBeAbleToCreateBlankPassword() {

        final EncryptedPassword password = new EncryptedPassword();

        assertThat(password.getWhereFor(), is(nullValue(String.class)));
        assertThat(password.getUsername(), is(nullValue(String.class)));
        assertThat(password.getPassword(), is(nullValue(byte[].class)));
        assertThat(password.getSecuredBy(), is(nullValue(SymmetricLock.class)));
    }
}