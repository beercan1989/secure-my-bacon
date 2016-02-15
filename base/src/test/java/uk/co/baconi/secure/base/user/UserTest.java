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

package uk.co.baconi.secure.base.user;

import org.junit.Test;
import uk.co.baconi.secure.base.lock.AsymmetricLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class UserTest {

    @Test
    public void shouldBeAbleToReadProperties() {

        final String name = "beercan1989";
        final User user = new User(name);

        assertThat(user.getId(), is(nullValue()));
        assertThat(user.getName(), is(equalTo(name)));
        assertThat(user.getShared(), is(emptyCollectionOf(AsymmetricLock.class)));
    }

    @Test
    public void shouldBeAbleToGetSharedWithLinks() {

        final String name = "beercan1989";
        final AsymmetricLock asymmetricLock = mock(AsymmetricLock.class);
        final AsymmetricLock asymmetricLock2 = mock(AsymmetricLock.class);

        final User user = new User(name).sharedWith(asymmetricLock);

        assertThat(user.getShared(), contains(asymmetricLock));

        user.sharedWith(asymmetricLock2);

        assertThat(user.getShared(), containsInAnyOrder(asymmetricLock, asymmetricLock2));
    }

    @Test
    public void shouldImplementEqualsCorrectly() {

        final String name = "beercan1989";

        final User user1 = new User(name);
        final User user2 = new User(name);

        assertThat(user1, is(equalTo(user2)));
        assertThat(user1.hashCode(), is(equalTo(user2.hashCode())));

        final User user3 = new User("beercan");

        assertThat(user1, is(not(equalTo(user3))));
        assertThat(user2, is(not(equalTo(user3))));

        assertThat(user1.hashCode(), is(not(equalTo(user3.hashCode()))));
        assertThat(user2.hashCode(), is(not(equalTo(user3.hashCode()))));

        final AsymmetricLock asymmetricLock = mock(AsymmetricLock.class);

        user1.sharedWith(asymmetricLock);

        assertThat(user1, is(equalTo(user2)));
        assertThat(user1.hashCode(), is(equalTo(user2.hashCode())));
    }

    @Test
    public void shouldHaveNiceToStringRepresentation() {

        final User user = new User("beercan1989");

        final String userAsString = user.toString();

        assertThat(userAsString, containsString("id=null,"));
        assertThat(userAsString, containsString("name='beercan1989'"));
    }

    @Test
    public void shouldBeAbleToCreateBlankUser() {

        final User user = new User();

        assertThat(user.getId(), is(nullValue(Long.class)));
        assertThat(user.getName(), is(nullValue(String.class)));
        assertThat(user.getShared(), is(emptyCollectionOf(AsymmetricLock.class)));
    }
}