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

package uk.co.baconi.secure;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("uk.co.baconi.secure")
@EnableNeo4jRepositories({
        "uk.co.baconi.secure.bag",
        "uk.co.baconi.secure.lock",
        "uk.co.baconi.secure.password",
        "uk.co.baconi.secure.user"
})
@EnableTransactionManagement
public class IntegrationTestConfiguration extends Neo4jConfiguration {

    @Override
    public Neo4jServer neo4jServer() {
        return new RemoteServer("http://localhost:7474");
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory("uk.co.baconi.secure");
    }

}
