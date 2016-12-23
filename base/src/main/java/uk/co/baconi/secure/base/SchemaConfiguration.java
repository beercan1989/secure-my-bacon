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

import static java.util.Collections.emptyMap;

@Slf4j
@Configuration
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__({@Autowired}))
public class SchemaConfiguration {

    private final Neo4jOperations neo4jOperations;
    private final Neo4JProperties properties;

    @PostConstruct
    public void perform() {

        if (properties.getEnableAutoSchemaCreationOnStartUp()) {
            createIndexes();
        } else {
            log.info("enableAutoSchemaCreationOnStartUp: DISABLED");
        }
    }

    private void createIndexes() {
        log.info("createIndexes: START");

        createConstraint("CREATE CONSTRAINT ON (u:User) ASSERT u.name IS UNIQUE");
        createConstraint("CREATE CONSTRAINT ON (b:Bag) ASSERT b.name IS UNIQUE");
        createConstraint("CREATE CONSTRAINT ON (p:Password) ASSERT p.uuid IS UNIQUE");

        log.info("createIndexes: END");
    }

    private void createConstraint(final String constraint) {

        log.debug("createConstraint: {}", constraint);

        final Result result = neo4jOperations.query(constraint, emptyMap());

        if (log.isDebugEnabled()) {
            log.debug("createConstraintResults: {}", ToStringBuilder.reflectionToString(result.queryStatistics()));
        }
    }
}
