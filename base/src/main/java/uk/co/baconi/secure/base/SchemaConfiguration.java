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

package uk.co.baconi.secure.base;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.neo4j.ogm.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.template.Neo4jOperations;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Slf4j
@Configuration
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor=@__({@Autowired}))
public class SchemaConfiguration {

    private final Neo4jOperations neo4jOperations;

    @PostConstruct
    public void createConstrains() {

        log.info("createUniqueIndexes: START");

        // User
        createConstraint("CREATE CONSTRAINT ON (user:User) ASSERT user.name IS UNIQUE");
        createConstraint("CREATE CONSTRAINT ON (user:User) ASSERT exists(user.name)");

        // Group
        createConstraint("CREATE CONSTRAINT ON (group:Group) ASSERT group.name IS UNIQUE");
        createConstraint("CREATE CONSTRAINT ON (group:Group) ASSERT exists(group.name)");

        log.info("createUniqueIndexes: END");
    }

    private void createConstraint(final String constraint) {

        log.debug("createConstraint: {}", constraint);

        final Result result = neo4jOperations.query(constraint, Collections.emptyMap());

        if(log.isDebugEnabled()) {
            log.debug("createConstraintResults: {}", ToStringBuilder.reflectionToString(result.queryStatistics()));
        }
    }
}
