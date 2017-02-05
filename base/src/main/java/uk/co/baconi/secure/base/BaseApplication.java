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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.config.DriverConfiguration;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.security.Security;

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableConfigurationProperties
@Import(SchemaConfiguration.class)
@EnableNeo4jRepositories({
        "uk.co.baconi.secure.base.bag",
        "uk.co.baconi.secure.base.lock",
        "uk.co.baconi.secure.base.password",
        "uk.co.baconi.secure.base.user",
        "uk.co.baconi.secure.base.repository"
})
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__({@Autowired}))
public class BaseApplication {

    private final Neo4JProperties neo4JProperties;

    @PostConstruct
    public void registerSecurityProviders() {
        log.info("Adding Security Provider: BouncyCastle");

        Security.addProvider(new BouncyCastleProvider());
    }

    @Bean
    public Configuration configuration() {
        log.info("Creating Neo4J Configuration with Neo4JProperties: {}", neo4JProperties);

        final Configuration config = new Configuration();

        final DriverConfiguration driverConfiguration = config.driverConfiguration();

        if (isNotEmpty(neo4JProperties.getDriver())) {
            driverConfiguration.setDriverClassName(neo4JProperties.getDriver());
        }

        if (isNotEmpty(neo4JProperties.getUsername()) && isNotEmpty(neo4JProperties.getPassword())) {
            driverConfiguration.setCredentials(neo4JProperties.getUsername(), neo4JProperties.getPassword());
        }

        if (isNotEmpty(neo4JProperties.getUri())) {
            driverConfiguration.setURI(neo4JProperties.getUri());
        }

        if (neo4JProperties.getConnectionPoolSize() != null) {
            driverConfiguration.setConnectionPoolSize(neo4JProperties.getConnectionPoolSize());
        }

        if (isNotEmpty(neo4JProperties.getEncryptionLevel())) {
            driverConfiguration.setEncryptionLevel(neo4JProperties.getEncryptionLevel());
        }

        if (isNotEmpty(neo4JProperties.getTrustStrategy())) {
            driverConfiguration.setTrustStrategy(neo4JProperties.getTrustStrategy());

            if (isNotEmpty(neo4JProperties.getTrustCertificateFile())) {
                driverConfiguration.setTrustCertFile(neo4JProperties.getTrustCertificateFile());
            }
        }

        return config;
    }

    @Bean
    public SessionFactory sessionFactory() {
        log.info("Creating: SessionFactory");

        return new SessionFactory(
                configuration(),
                "uk.co.baconi.secure.base.bag",
                "uk.co.baconi.secure.base.lock",
                "uk.co.baconi.secure.base.password",
                "uk.co.baconi.secure.base.user",
                "uk.co.baconi.secure.base.repository"
        );
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }

    private boolean isNotEmpty(final String string) {
        return string != null && !string.trim().isEmpty();
    }
}
