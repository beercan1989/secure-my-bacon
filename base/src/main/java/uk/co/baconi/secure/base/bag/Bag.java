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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLock;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(exclude = {"id", "shared", "secured"})
@ToString(exclude = {"shared", "secured"})
@NoArgsConstructor
public class Bag {

    @GraphId
    @Deprecated
    private Long id;

    @Setter
    private String name;

    @Setter
    @JsonIgnore
    private byte[] publicKey;

    @JsonIgnore
    @Relationship(type = AsymmetricLock.SHARED_WITH)
    private Set<AsymmetricLock> shared = new HashSet<>();

    @JsonIgnore
    @Relationship(type = SymmetricLock.SECURED_BY, direction = Relationship.INCOMING)
    private Set<SymmetricLock> secured = new HashSet<>();


    public Bag(final String name, final byte[] publicKey) {
        this.name = name;
        this.publicKey = publicKey;
    }

    public Bag sharedWith(final AsymmetricLock sharedWith) {

        this.shared.add(sharedWith);

        return this;
    }

    public Bag securedWith(final SymmetricLock securedWith) {

        this.secured.add(securedWith);

        return this;
    }

}
