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

package uk.co.baconi.secure.lock;

import org.neo4j.ogm.annotation.*;
import uk.co.baconi.secure.bag.Bag;
import uk.co.baconi.secure.password.Password;

import java.util.Arrays;
import java.util.Objects;

@RelationshipEntity(type = SymmetricLock.SECURED_BY)
public class SymmetricLock {

    public static final String SECURED_BY = "SECURED_BY";

    @GraphId
    private Long id;

    @Property
    private byte[] key;

    @StartNode
    private Password password;

    @EndNode
    private Bag bag;

    // Here for Neo4J annotations
    public SymmetricLock() {
    }

    public SymmetricLock(final Password password, final Bag bag, final byte[] key) {
        this.password = password;
        this.bag = bag;

        this.key = key;  // TODO - Encryption with the source's public key

        //this.password;
        this.bag.securedWith(this);
        this.password.securedBy(this);
    }

    public Long getId() {
        return id;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public Password getPassword() {
        return password;
    }

    public Bag getBag() {
        return bag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymmetricLock that = (SymmetricLock) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(password, that.password) &&
                Objects.equals(bag, that.bag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, password, bag);
    }

    @Override
    public String toString() {
        return "SymmetricLock{" +
                "id=" + id +
                ", key=" + Arrays.toString(key) +
                ", password=" + password +
                ", bag=" + bag +
                '}';
    }
}
