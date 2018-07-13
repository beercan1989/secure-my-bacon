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
import org.neo4j.ogm.exception.ConnectionException;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyMap;

@Slf4j
@Configuration
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__({@Autowired}))
public class SchemaConfiguration {

    private final Session neo4jSession;
    private final Neo4JProperties properties;

    @PostConstruct
    public void perform() throws InterruptedException {

        if (properties.isAutoSchemaCreationOnStartUpEnabled()) {
            final int timeout = properties.getAutoSchemaCreationOnStartUpTimeout();
            perform(LocalDateTime.now().plus(timeout, ChronoUnit.MILLIS));
        } else {
            log.info("enableAutoSchemaCreationOnStartUp: DISABLED");
        }
    }

    private void perform(final LocalDateTime timeoutTime) throws InterruptedException {
        try {
            createIndexes();
        } catch (final ConnectionException exception) {

            if (LocalDateTime.now().isBefore(timeoutTime)) {
                TimeUnit.MILLISECONDS.sleep(500);
                log.trace("Retrying perform block.");
                perform(timeoutTime);
            } else {
                log.trace("Giving up on perform block.");
                throw exception;
            }
        }
    }

    private void createIndexes() {
        log.info("createIndexes: START");

        createConstraint("CREATE CONSTRAINT ON (u:User) ASSERT u.name IS UNIQUE");
        createConstraint("CREATE CONSTRAINT ON (b:Bag) ASSERT b.name IS UNIQUE");
        createConstraint("CREATE CONSTRAINT ON (p:EncryptedPassword) ASSERT p.uuid IS UNIQUE");

        log.info("createIndexes: END");
    }

    private void createConstraint(final String constraint) {

        log.debug("createConstraint: {}", constraint);

        final Result result = neo4jSession.query(constraint, emptyMap());

        if (log.isDebugEnabled()) {
            log.debug("createConstraintResults: {}", ToStringBuilder.reflectionToString(result.queryStatistics()));
        }
    }
}
