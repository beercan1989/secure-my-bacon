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

package uk.co.baconi.secure.base;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.ogm.exception.ConnectionException;
import org.neo4j.ogm.session.Session;

import static org.mockito.Mockito.*;

public class SchemaConfigurationTest {


    private final Session neo4jSession = mock(Session.class);
    private final Neo4JProperties neo4jProperties = mock(Neo4JProperties.class);

    private final SchemaConfiguration underTest = new SchemaConfiguration(neo4jSession, neo4jProperties);

    @Before
    public void before() {
        reset(neo4jSession, neo4jProperties);
    }

    @Test
    public void shouldNotRunSchemaMigrationsIfDisabled() throws InterruptedException {

        when(neo4jProperties.isAutoSchemaCreationOnStartUpEnabled()).thenReturn(false);

        underTest.perform();

        verifyZeroInteractions(neo4jSession);
    }

    @Test(expected = ConnectionException.class)
    public void shouldRetryOnlyOnNeo4jConnectionExceptions() throws InterruptedException {

        when(neo4jProperties.isAutoSchemaCreationOnStartUpEnabled()).thenReturn(true);
        when(neo4jProperties.getAutoSchemaCreationOnStartUpTimeout()).thenReturn(250);
        when(neo4jSession.query(anyString(), anyMap())).thenThrow(ConnectionException.class);

        try {
            underTest.perform();
        } finally {
            verify(neo4jSession, atLeast(2)).query(anyString(), anyMap());
        }
    }
}
