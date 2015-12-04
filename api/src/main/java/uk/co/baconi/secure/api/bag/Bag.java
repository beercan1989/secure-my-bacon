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

package uk.co.baconi.secure.api.bag;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.baconi.secure.api.lock.AsymmetricLock;
import uk.co.baconi.secure.api.lock.SymmetricLock;

import java.util.Set;

public class Bag {

    @GraphId
    private Long id;

    @Property
    private String name;

    @Property
    private byte[] publicKey;

    @Relationship(type = "SHARED_WITH")
    private Set<AsymmetricLock> shared;

    @Relationship(type = "SECURED_BY", direction = Relationship.INCOMING)
    private Set<SymmetricLock> secured;
}
