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
import uk.co.baconi.secure.base.user.User;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class AsymmetricLockTest extends BaseUnitTest {

    @Test
    public void shouldBeAbleToChangePrivateKey() {
        final User user = mock(User.class);
        final Bag bag = mock(Bag.class);
        final AsymmetricLock lock = new AsymmetricLock(bag, user, "".getBytes());

        verify(user).sharedWith(lock);
        verify(bag).sharedWith(lock);

        final byte[] newPrivateKey = "new private key".getBytes();
        lock.setPrivateKey(newPrivateKey);
        assertThat(lock.getPrivateKey(), is(equalTo(newPrivateKey)));


        verifyNoMoreInteractions(user, bag);
    }

    @Test
    public void shouldBeAddedToBothUserAndBagOnCreation() {

        final User user = mock(User.class);
        final Bag bag = mock(Bag.class);
        final byte[] privateKey = "private key".getBytes();

        final AsymmetricLock lock = new AsymmetricLock(bag, user, privateKey);

        verify(user).sharedWith(lock);
        verify(bag).sharedWith(lock);

        assertThat(lock.getId(), is(nullValue(Long.class)));
        assertThat(lock.getBag(), is(equalTo(bag)));
        assertThat(lock.getUser(), is(equalTo(user)));
        assertThat(lock.getPrivateKey(), is(equalTo(privateKey)));

        verifyNoMoreInteractions(user, bag);
    }

    @Test
    public void shouldImplementEqualsCorrectly() {

        final User user = mock(User.class);
        final Bag bag = mock(Bag.class);
        final byte[] privateKey = "private key".getBytes();

        final AsymmetricLock lock1 = new AsymmetricLock(bag, user, privateKey);
        final AsymmetricLock lock2 = new AsymmetricLock(bag, user, privateKey);

        assertThat(lock1, is(equalTo(lock2)));
        assertThat(lock1.hashCode(), is(equalTo(lock2.hashCode())));

        final AsymmetricLock lock3 = new AsymmetricLock(bag, mock(User.class), privateKey);

        assertThat(lock1, is(not(equalTo(lock3))));
        assertThat(lock2, is(not(equalTo(lock3))));
        assertThat(lock1.hashCode(), is(not(equalTo(lock3.hashCode()))));
        assertThat(lock2.hashCode(), is(not(equalTo(lock3.hashCode()))));

        final AsymmetricLock lock4 = new AsymmetricLock(mock(Bag.class), user, privateKey);

        assertThat(lock1, is(not(equalTo(lock4))));
        assertThat(lock2, is(not(equalTo(lock4))));
        assertThat(lock3, is(not(equalTo(lock4))));
        assertThat(lock1.hashCode(), is(not(equalTo(lock4.hashCode()))));
        assertThat(lock2.hashCode(), is(not(equalTo(lock4.hashCode()))));
        assertThat(lock3.hashCode(), is(not(equalTo(lock4.hashCode()))));

        final AsymmetricLock lock5 = new AsymmetricLock(bag, user, "diffent private key".getBytes());

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

        final User user = mock(User.class);
        final Bag bag = mock(Bag.class);
        final byte[] privateKey = "private key".getBytes();

        final AsymmetricLock lock = new AsymmetricLock(bag, user, privateKey);

        final String lockAsString = lock.toString();

        assertThat(lockAsString, containsString("id=null,"));
        assertThat(lockAsString, not(containsString("privateKey=")));
        assertThat(lockAsString, containsString("bag=" + bag + ','));
        assertThat(lockAsString, containsString("user=" + user));
    }

    @Test
    public void shouldBeAbleToCreateBlankAsymmetricLock() {

        final AsymmetricLock lock = new AsymmetricLock();

        assertThat(lock.getId(), is(nullValue(Long.class)));
        assertThat(lock.getBag(), is(nullValue(Bag.class)));
        assertThat(lock.getUser(), is(nullValue(User.class)));
        assertThat(lock.getPrivateKey(), is(nullValue(byte[].class)));
    }
}