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
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import static uk.co.baconi.secure.base.lock.SymmetricLock.SECURED_BY;
import static uk.co.baconi.secure.base.lock.AsymmetricLock.SHARED_WITH;

@Repository
public interface PasswordGraphRepository extends GraphRepository<Password> {

    @Query("MATCH (password:Password)<-[:"+SECURED_BY+"]-(bag)<-[:"+SHARED_WITH+"]-(user)" +
           "WHERE id(password)={passwordId} AND id(user)={userId}" +
           "RETURN password")
    Password getPasswordByUser(@Param("passwordId") final Long passwordId, @Param("userId")  final Long userId);

}
