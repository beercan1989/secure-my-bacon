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

package uk.co.baconi.secure.base.password;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.baconi.secure.base.lock.SymmetricLock;

@NoArgsConstructor
@NodeEntity
@Getter
@EqualsAndHashCode(exclude={"id", "securedBy"})
@ToString(exclude = {"password", "securedBy"})
public class Password {

    @GraphId
    private Long id;

    @Setter
    @Property
    private String whereFor;

    @Setter
    @Property
    private String username;

    @Setter
    @Property
    private byte[] password;

    @JsonIgnore
    @Relationship(type = SymmetricLock.SECURED_BY)
    private SymmetricLock securedBy;

    public Password(final String whereFor, final String username, final byte[] password) {
        this.whereFor = whereFor;
        this.username = username;
        this.password = password;  // TODO - Encryption with the target's public key
    }

    public Password securedBy(final SymmetricLock securedBy) {

        this.securedBy = securedBy;

        return this;
    }
}
