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

package uk.co.baconi.secure.api.lock;

import org.neo4j.ogm.annotation.*;
import uk.co.baconi.secure.api.bag.Bag;
import uk.co.baconi.secure.api.password.Password;
import uk.co.baconi.secure.api.user.User;

@RelationshipEntity(type = "SECURED_BY")
public class SymmetricLock {

    @GraphId
    private Long relationshipId;

    @Property
    private byte[] key;

    @StartNode
    private Password password;

    @EndNode
    private Bag bag;

}
