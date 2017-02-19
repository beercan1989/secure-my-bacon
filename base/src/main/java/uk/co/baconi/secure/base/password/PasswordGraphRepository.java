/*
 * Copyright 2016 James Bacon
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

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.co.baconi.secure.base.common.SmbGraphRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PasswordGraphRepository extends SmbGraphRepository<Password> {

    Password findByUuid(final UUID uuid);

    @Query("MATCH (p:Password {uuid:{uuid}})-[:SECURED_BY]-(:Bag)-[:SHARED_WITH]-(u:User {name:{userName}}) RETURN p")
    Password getPasswordForUser(@Param("uuid") final UUID passwordUuid, @Param("userName") final String userName);

    @Query("MATCH (p:Password)-[:SECURED_BY]-(:Bag)-[:SHARED_WITH]-(:User {name:{userName}}) RETURN p")
    List<Password> getPasswordsForUser(@Param("userName") final String userName);

    // Spring Data Neo4J - Hopper-SR1 does not support pageable in customer queries.
    // Page<Password> getPasswordsForUser(@Param("userName") final String userName, final Pageable pageRequest);

}
