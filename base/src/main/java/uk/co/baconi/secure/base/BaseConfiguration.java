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

import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableNeo4jRepositories({
        "uk.co.baconi.secure.base.bag",
        "uk.co.baconi.secure.base.lock",
        "uk.co.baconi.secure.base.password",
        "uk.co.baconi.secure.base.user"
})
public class BaseConfiguration extends Neo4jConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BaseConfiguration.class);

    @Autowired
    private BaseNeo4JProperties neo4JProperties;

    @Override
    public Neo4jServer neo4jServer() {

        LOG.info("Properties: {}", neo4JProperties);

        return new RemoteServer(neo4JProperties.getUrl(), neo4JProperties.getUsername(), neo4JProperties.getPassword());
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory("uk.co.baconi.secure.base");
    }

}
