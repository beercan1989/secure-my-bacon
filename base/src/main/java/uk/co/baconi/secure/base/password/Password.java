/*
 * Copyright 2017 James Bacon
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
import org.neo4j.ogm.annotation.typeconversion.Convert;
import uk.co.baconi.secure.base.common.UuidConverter;
import uk.co.baconi.secure.base.lock.SymmetricLock;

import java.util.UUID;

@Getter
@NoArgsConstructor
@NodeEntity(label = "Password")
@EqualsAndHashCode(of = "uuid")
@ToString(exclude = {"password", "securedBy"})
public abstract class Password<A extends Password<A>> {

    @GraphId
    @Deprecated
    @JsonIgnore
    private Long id;

    @Property
    @Convert(UuidConverter.class)
    private UUID uuid;

    @Property
    @Setter(value = AccessLevel.PROTECTED)
    private String whereFor;

    @Property
    @Setter(value = AccessLevel.PROTECTED)
    private String username;

    @Property
    @Setter(value = AccessLevel.PROTECTED)
    private byte[] password;

    @JsonIgnore
    @Relationship(type = SymmetricLock.SECURED_BY)
    private SymmetricLock securedBy;

    protected Password(final String whereFor, final String username, final byte[] password) {
        this.uuid = UUID.randomUUID();
        this.whereFor = whereFor;
        this.username = username;
        this.password = password;  // TODO - Encryption with the target's public key
    }

    protected Password(final UUID uuid, final String whereFor, final String username, final byte[] password) {
        this.uuid = uuid;
        this.whereFor = whereFor;
        this.username = username;
        this.password = password;  // TODO - Encryption with the target's public key
    }

    @SuppressWarnings("unchecked")
    public A securedBy(final SymmetricLock securedBy) {

        this.securedBy = securedBy;

        return (A) this;
    }
}
