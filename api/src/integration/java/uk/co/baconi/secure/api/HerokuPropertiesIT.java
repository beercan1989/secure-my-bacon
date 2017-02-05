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

package uk.co.baconi.secure.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.baconi.secure.base.BaseApplication;
import uk.co.baconi.secure.base.Neo4JProperties;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("heroku")
@SpringBootTest(
        classes = BaseApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "GRAPHENEDB_BOLT_URL=bolt://heroku-integration-test:7687",
                "GRAPHENEDB_BOLT_USER=heroku",
                "GRAPHENEDB_BOLT_PASSWORD=eiyū",
                "neo4j.autoSchemaCreationOnStartUpEnabled=false"
        }
)
public class HerokuPropertiesIT {

    @Autowired
    private Neo4JProperties neo4JProperties;

    @Test
    public void shouldPickUpEnvironmentBasedProperties() {
        assertThat(neo4JProperties.getUri()).isEqualTo("bolt://heroku-integration-test:7687");
        assertThat(neo4JProperties.getUsername()).isEqualTo("heroku");
        assertThat(neo4JProperties.getPassword()).isEqualTo("eiyū");
    }

    @Test
    public void shouldPickUpStaticProperties() {
        assertThat(neo4JProperties.getDriver()).isEqualTo("org.neo4j.ogm.drivers.bolt.driver.BoltDriver");
        assertThat(neo4JProperties.getEncryptionLevel()).isEqualTo("REQUIRED");
    }
}
