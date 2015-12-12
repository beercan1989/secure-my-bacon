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

package uk.co.baconi.secure.api.password;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.baconi.secure.api.common.Entity;
import uk.co.baconi.secure.api.lock.SymmetricLock;

@NodeEntity
public class Password extends Entity {

    @Property
    private String whereFor;

    @Property
    private String username;

    @Property
    private String password;

    @Relationship(type = "SECURED_BY")
    private SymmetricLock securedBy;

    public Password() {
    }

    public Password(final String username, final String password, final String whereFor, final SymmetricLock securedBy) {
        this.username = username;
        this.password = password;
        this.whereFor = whereFor;
        this.securedBy = securedBy;
    }

    public String getWhereFor() {
        return whereFor;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public SymmetricLock getSecuredBy() {
        return securedBy;
    }
}
