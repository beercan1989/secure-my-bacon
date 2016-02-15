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

package uk.co.baconi.secure.base.bag;

import org.hamcrest.Matchers;
import org.junit.Test;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLock;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class BagTest {

    @Test
    public void shouldBeAbleToReadProperties() {

        final String name = "Substeps";
        final byte[] publicKey = "public key".getBytes();
        final Bag bag = new Bag(name, publicKey);

        assertThat(bag.getId(), is(nullValue()));
        assertThat(bag.getName(), is(equalTo(name)));
        assertThat(bag.getPublicKey(), is(equalTo(publicKey)));
        assertThat(bag.getSecured(), is(emptyCollectionOf(SymmetricLock.class)));
        assertThat(bag.getShared(), is(emptyCollectionOf(AsymmetricLock.class)));
    }

    @Test
    public void shouldBeAbleToChangeProperties() {
        final Bag bag = new Bag("", "".getBytes());

        final String newName = "Substeps.org";
        bag.setName(newName);
        assertThat(bag.getName(), is(equalTo(newName)));

        final byte[] newPublicKey = "new public key".getBytes();
        bag.setPublicKey(newPublicKey);
        assertThat(bag.getPublicKey(), is(equalTo(newPublicKey)));
    }

    @Test
    public void shouldBeAbleToGetSharedWithLinks() {
        final Bag bag = new Bag("", "".getBytes());

        final AsymmetricLock asymmetricLock = mock(AsymmetricLock.class);
        final AsymmetricLock asymmetricLock2 = mock(AsymmetricLock.class);

        bag.sharedWith(asymmetricLock);

        assertThat(bag.getShared(), contains(asymmetricLock));

        bag.sharedWith(asymmetricLock2);

        assertThat(bag.getShared(), containsInAnyOrder(asymmetricLock, asymmetricLock2));
    }

    @Test
    public void shouldBeAbleToGetSecuredLinks() {
        final Bag bag = new Bag("", "".getBytes());

        final SymmetricLock symmetricLock = mock(SymmetricLock.class);
        final SymmetricLock symmetricLock2 = mock(SymmetricLock.class);

        bag.securedWith(symmetricLock);

        assertThat(bag.getSecured(), contains(symmetricLock));

        bag.securedWith(symmetricLock2);

        assertThat(bag.getSecured(), containsInAnyOrder(symmetricLock, symmetricLock2));
    }

    @Test
    public void shouldImplementEqualsCorrectly() {

        final String name = "Substeps";
        final byte[] publicKey = "public key".getBytes();

        final Bag bag1 = new Bag(name, publicKey);
        final Bag bag2 = new Bag(name, publicKey);

        assertThat(bag1, is(equalTo(bag2)));
        assertThat(bag1.hashCode(), is(equalTo(bag2.hashCode())));

        final Bag bag3 = new Bag("Cucumber", publicKey);

        assertThat(bag1, is(not(equalTo(bag3))));
        assertThat(bag2, is(not(equalTo(bag3))));

        assertThat(bag1.hashCode(), is(not(equalTo(bag3.hashCode()))));
        assertThat(bag2.hashCode(), is(not(equalTo(bag3.hashCode()))));

        final Bag bag4 = new Bag(name, "another public key".getBytes());

        assertThat(bag1, is(not(equalTo(bag4))));
        assertThat(bag2, is(not(equalTo(bag4))));
        assertThat(bag3, is(not(equalTo(bag4))));

        assertThat(bag1.hashCode(), is(not(equalTo(bag4.hashCode()))));
        assertThat(bag2.hashCode(), is(not(equalTo(bag4.hashCode()))));
        assertThat(bag3.hashCode(), is(not(equalTo(bag4.hashCode()))));

        final SymmetricLock symmetricLock = mock(SymmetricLock.class);

        bag1.securedWith(symmetricLock);

        assertThat(bag1, is(equalTo(bag2)));
        assertThat(bag1.hashCode(), is(equalTo(bag2.hashCode())));

        final AsymmetricLock asymmetricLock = mock(AsymmetricLock.class);

        bag2.sharedWith(asymmetricLock);

        assertThat(bag1, is(equalTo(bag2)));
        assertThat(bag1.hashCode(), is(equalTo(bag2.hashCode())));
    }

    @Test
    public void shouldHaveNiceToStringRepresentation() {

        final Bag bag = new Bag("Substeps", "public key".getBytes());

        final String bagAsString = bag.toString();

        assertThat(bagAsString, containsString("id=null,"));
        assertThat(bagAsString, containsString("name='Substeps',"));
        assertThat(bagAsString, containsString("publicKey=" + Arrays.toString("public key".getBytes())));
    }

    @Test
    public void shouldBeAbleToCreateBlankBag() {

        final Bag bag = new Bag();

        assertThat(bag.getId(), is(nullValue(Long.class)));
        assertThat(bag.getName(), is(nullValue(String.class)));
        assertThat(bag.getPublicKey(), is(nullValue(byte[].class)));
        assertThat(bag.getSecured(), is(emptyCollectionOf(SymmetricLock.class)));
        assertThat(bag.getShared(), is(emptyCollectionOf(AsymmetricLock.class)));
    }
}