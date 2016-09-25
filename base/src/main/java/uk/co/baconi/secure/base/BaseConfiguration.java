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
import org.neo4j.ogm.config.DriverConfiguration;
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
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor=@__({@Autowired}))
public class BaseConfiguration extends Neo4jConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BaseConfiguration.class);

    private final BaseNeo4JProperties neo4JProperties;

    @Bean
    public org.neo4j.ogm.config.Configuration getNeo4jConfiguration() {
        LOG.info("Properties: {}", neo4JProperties);

        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();

        final DriverConfiguration driverConfiguration = config.driverConfiguration();

        if( isNotEmpty(neo4JProperties.getDriver()) ) {
            driverConfiguration.setDriverClassName(neo4JProperties.getDriver());
        }

        if( isNotEmpty(neo4JProperties.getUsername()) && isNotEmpty(neo4JProperties.getPassword()) ) {
            driverConfiguration.setCredentials(neo4JProperties.getUsername(), neo4JProperties.getPassword());
        }

        if( isNotEmpty(neo4JProperties.getUrl()) ) {
            driverConfiguration.setURI(neo4JProperties.getUrl());
        }

        if( neo4JProperties.getConnectionPoolSize() != null ) {
            driverConfiguration.setConnectionPoolSize(neo4JProperties.getConnectionPoolSize());
        }

        if( isNotEmpty(neo4JProperties.getEncryptionLevel()) ) {
            driverConfiguration.setEncryptionLevel(neo4JProperties.getEncryptionLevel());
        }

        if( isNotEmpty(neo4JProperties.getTrustStrategy()) ) {
            driverConfiguration.setTrustStrategy(neo4JProperties.getTrustStrategy());

            if( isNotEmpty(neo4JProperties.getTrustCertificateFile()) ) {
                driverConfiguration.setTrustCertFile(neo4JProperties.getTrustCertificateFile());
            }
        }

        return config;
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory(
                getNeo4jConfiguration(),
                "uk.co.baconi.secure.base.bag",
                "uk.co.baconi.secure.base.lock",
                "uk.co.baconi.secure.base.password",
                "uk.co.baconi.secure.base.user"
        );
    }

    private boolean isNotEmpty(final String string) {
        return string != null && !string.trim().isEmpty();
    }
}
