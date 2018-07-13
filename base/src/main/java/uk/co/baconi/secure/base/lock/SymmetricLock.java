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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.neo4j.ogm.annotation.*;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.password.EncryptedPassword;

@Getter
@NoArgsConstructor
@ToString(exclude = "key")
@EqualsAndHashCode(exclude = {"id"})
@RelationshipEntity(type = SymmetricLock.SECURED_BY)
public class SymmetricLock {

    public static final String SECURED_BY = "SECURED_BY";

    @GraphId
    @Deprecated
    @JsonIgnore
    private Long id;

    @Setter
    @JsonIgnore
    private SymmetricCipher type;

    @Setter
    @Property
    @JsonIgnore
    private byte[] key;

    @StartNode
    private EncryptedPassword password;

    @EndNode
    private Bag bag;

    public SymmetricLock(final EncryptedPassword password, final Bag bag, final byte[] key, final SymmetricCipher type) {
        this.password = password;
        this.bag = bag;

        this.key = key;  // TODO - Encryption with the source's public key
        this.type = type;

        this.bag.securedWith(this);
        this.password.securedBy(this);
    }

}
