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

package uk.co.baconi.secure.base.bag;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Bag {

    @GraphId
    private Long id;

    @Property
    private String name;

    @Property
    private byte[] publicKey;

    @Relationship(type = AsymmetricLock.SHARED_WITH)
    private Set<AsymmetricLock> shared = new HashSet<>();

    @Relationship(type = SymmetricLock.SECURED_BY, direction = Relationship.INCOMING)
    private Set<SymmetricLock> secured = new HashSet<>();

    // Here for Neo4J annotations
    public Bag() {
    }

    public Bag(final String name, final byte[] publicKey){
        this.name = name;
        this.publicKey = publicKey;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public Bag sharedWith(final AsymmetricLock sharedWith){

        this.shared.add(sharedWith);

        return this;
    }

    public Set<AsymmetricLock> getShared() {
        return shared;
    }

    public Bag securedWith(final SymmetricLock securedWith) {

        this.secured.add(securedWith);

        return this;
    }

    public Set<SymmetricLock> getSecured() {
        return secured;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bag bag = (Bag) o;
        return Objects.equals(name, bag.name) &&
                Objects.equals(publicKey, bag.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publicKey);
    }

    @Override
    public String toString() {
        return "Bag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", publicKey=" + Arrays.toString(publicKey) +
                '}';
    }
}
