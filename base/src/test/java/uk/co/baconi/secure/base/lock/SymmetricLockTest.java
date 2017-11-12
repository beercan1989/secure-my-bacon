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

package uk.co.baconi.secure.base.lock;

import org.junit.Test;
import uk.co.baconi.secure.base.BaseUnitTest;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.password.EncryptedPassword;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class SymmetricLockTest extends BaseUnitTest {

    @Test
    public void shouldBeAbleToChangeKey() {
        final EncryptedPassword password = mock(EncryptedPassword.class);
        final Bag bag = mock(Bag.class);
        final SymmetricLock lock = new SymmetricLock(password, bag, "".getBytes(), SymmetricCipher.AES_CBC_PKCS7);

        verify(password).securedBy(lock);
        verify(bag).securedWith(lock);

        final byte[] newKey = "new key".getBytes();
        lock.setKey(newKey);
        assertThat(lock.getKey(), is(equalTo(newKey)));

        verifyNoMoreInteractions(password, bag);
    }

    @Test
    public void shouldBeAddedToBothUserAndBagOnCreation() {

        final EncryptedPassword password = mock(EncryptedPassword.class);
        final Bag bag = mock(Bag.class);
        final byte[] key = "symmetric key".getBytes();

        final SymmetricLock lock = new SymmetricLock(password, bag, key, SymmetricCipher.AES_CBC_PKCS7);

        verify(password).securedBy(lock);
        verify(bag).securedWith(lock);

        assertThat(lock.getId(), is(nullValue(Long.class)));
        assertThat(lock.getBag(), is(equalTo(bag)));
        assertThat(lock.getPassword(), is(equalTo(password)));
        assertThat(lock.getKey(), is(equalTo(key)));

        verifyNoMoreInteractions(password, bag);
    }

    @Test
    public void shouldImplementEqualsCorrectly() {

        final EncryptedPassword password = mock(EncryptedPassword.class);
        final Bag bag = mock(Bag.class);
        final byte[] key = "symmetric key".getBytes();

        final SymmetricLock lock1 = new SymmetricLock(password, bag, key, SymmetricCipher.AES_CBC_PKCS7);
        final SymmetricLock lock2 = new SymmetricLock(password, bag, key, SymmetricCipher.AES_CBC_PKCS7);

        assertThat(lock1, is(equalTo(lock2)));
        assertThat(lock1.hashCode(), is(equalTo(lock2.hashCode())));

        final SymmetricLock lock3 = new SymmetricLock(mock(EncryptedPassword.class), bag, key, SymmetricCipher.AES_CBC_PKCS7);

        assertThat(lock1, is(not(equalTo(lock3))));
        assertThat(lock2, is(not(equalTo(lock3))));
        assertThat(lock1.hashCode(), is(not(equalTo(lock3.hashCode()))));
        assertThat(lock2.hashCode(), is(not(equalTo(lock3.hashCode()))));

        final SymmetricLock lock4 = new SymmetricLock(password, mock(Bag.class), key, SymmetricCipher.AES_CBC_PKCS7);

        assertThat(lock1, is(not(equalTo(lock4))));
        assertThat(lock2, is(not(equalTo(lock4))));
        assertThat(lock3, is(not(equalTo(lock4))));
        assertThat(lock1.hashCode(), is(not(equalTo(lock4.hashCode()))));
        assertThat(lock2.hashCode(), is(not(equalTo(lock4.hashCode()))));
        assertThat(lock3.hashCode(), is(not(equalTo(lock4.hashCode()))));

        final SymmetricLock lock5 = new SymmetricLock(password, bag, "diffent symmetric key".getBytes(), SymmetricCipher.AES_CBC_PKCS7);

        assertThat(lock1, is(not(equalTo(lock5))));
        assertThat(lock2, is(not(equalTo(lock5))));
        assertThat(lock3, is(not(equalTo(lock5))));
        assertThat(lock4, is(not(equalTo(lock5))));
        assertThat(lock1.hashCode(), is(not(equalTo(lock5.hashCode()))));
        assertThat(lock2.hashCode(), is(not(equalTo(lock5.hashCode()))));
        assertThat(lock3.hashCode(), is(not(equalTo(lock5.hashCode()))));
        assertThat(lock4.hashCode(), is(not(equalTo(lock5.hashCode()))));
    }

    @Test
    public void shouldHaveNiceToStringRepresentation() {

        final EncryptedPassword password = mock(EncryptedPassword.class);
        final Bag bag = mock(Bag.class);
        final byte[] key = "symmetric key".getBytes();

        final SymmetricLock lock = new SymmetricLock(password, bag, key, SymmetricCipher.AES_CBC_PKCS7);

        final String lockAsString = lock.toString();

        assertThat(lockAsString, containsString("id=null,"));
        assertThat(lockAsString, not(containsString("key=")));
        assertThat(lockAsString, containsString("password=" + password + ','));
        assertThat(lockAsString, containsString("bag=" + bag));
    }

    @Test
    public void shouldBeAbleToCreateBlankSymmetricLock() {

        final SymmetricLock lock = new SymmetricLock();

        assertThat(lock.getId(), is(nullValue(Long.class)));
        assertThat(lock.getBag(), is(nullValue(Bag.class)));
        assertThat(lock.getPassword(), is(nullValue(EncryptedPassword.class)));
        assertThat(lock.getKey(), is(nullValue(byte[].class)));
    }
}