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
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import uk.co.baconi.secure.base.lock.SymmetricLock;

import java.util.Objects;

@NodeEntity
public class Password {

    @GraphId
    private Long id;

    @Property
    private String whereFor;

    @Property
    private String username;

    @Property
    private String password;

    @JsonIgnore
    @Relationship(type = SymmetricLock.SECURED_BY)
    private SymmetricLock securedBy;

    // Here for Neo4J annotations
    public Password() {
    }

    public Password(final String whereFor, final String username, final String password) {
        this.whereFor = whereFor;
        this.username = username;
        this.password = password;  // TODO - Encryption with the target's public key
    }

    public Long getId() {
        return id;
    }

    public String getWhereFor() {
        return whereFor;
    }

    public void setWhereFor(final String whereFor) {
        this.whereFor = whereFor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SymmetricLock getSecuredBy() {
        return securedBy;
    }

    public Password securedBy(final SymmetricLock securedBy) {

        this.securedBy = securedBy;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(whereFor, password1.whereFor) &&
                Objects.equals(username, password1.username) &&
                Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whereFor, username, password);
    }

    @Override
    public String toString() {
        return "Password{" +
                "id=" + id +
                ", whereFor='" + whereFor + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
