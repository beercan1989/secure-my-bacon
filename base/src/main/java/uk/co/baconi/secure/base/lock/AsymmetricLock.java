/*
 * Copyright 2015 James Bacon
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

import org.neo4j.ogm.annotation.*;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.user.User;

import java.util.Arrays;
import java.util.Objects;

@RelationshipEntity(type = AsymmetricLock.SHARED_WITH)
public class AsymmetricLock {

    public static final String SHARED_WITH = "SHARED_WITH";

    @GraphId
    private Long id;

    @Property
    private byte[] privateKey;

    @StartNode
    private Bag bag;

    @EndNode
    private User user;

    // Here for Neo4J annotations
    public AsymmetricLock() {
    }

    public AsymmetricLock(final Bag bag, final User user, final byte[] privateKey) {
        this.bag = bag;
        this.user = user;

        this.privateKey = privateKey; // TODO - Encryption with the target's public key

        this.bag.sharedWith(this);
        this.user.sharedWith(this);
    }

    public Long getId() {
        return id;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public Bag getBag() {
        return bag;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsymmetricLock that = (AsymmetricLock) o;
        return Objects.equals(privateKey, that.privateKey) &&
                Objects.equals(bag, that.bag) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privateKey, bag, user);
    }

    @Override
    public String toString() {
        return "AsymmetricLock{" +
                "id=" + id +
                ", privateKey=" + Arrays.toString(privateKey) +
                ", bag=" + bag +
                ", user=" + user +
                '}';
    }
}
