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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <h1>Neo4j Properties</h1>
 * <p>Configuration options based on the Neo4j documentation <a href="https://neo4j.com/docs/ogm-manual/current/reference/#reference:configuration">https://neo4j.com/docs/ogm-manual/current/reference/#reference:configuration</a></p>
 * <h2>Bolt</h2>
 * <ul>
 *     <li><strong>driver:</strong>org.neo4j.ogm.drivers.bolt.driver.BoltDriver</li>
 *     <li><strong>uri:</strong>bolt://localhost:7687</li>
 * </ul>
 * <h2>HTTP</h2>
 * <ul>
 *     <li><strong>driver:</strong>org.neo4j.ogm.drivers.http.driver.HttpDriver</li>
 *     <li><strong>uri:</strong>http://localhost:7474</li>
 * </ul>
 * <h2>Embedded - No Persistence</h2>
 * <ul>
 *     <li><strong>driver:</strong>org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver</li>
 * </ul>
 * <h2>Embedded - With Persistence</h2>
 * <ul>
 *     <li><strong>driver:</strong>org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver</li>
 *     <li><strong>uri:</strong>file:///var/tmp/neo4j.db</li>
 * </ul>
 * <h2>Credentials - For Bolt or HTTP connections</h2>
 * <ul>
 *     <li><strong>username:</strong>neo4j</li>
 *     <li><strong>password:</strong>password</li>
 * </ul>
 */
@Getter
@Setter
@Component
@ToString(exclude = "password")
@ConfigurationProperties(prefix = "neo4j")
public class Neo4JProperties {

    /**
     * The Neo4J driver to connect with: <br/>
     * <ul>
     *     <li>org.neo4j.ogm.drivers.bolt.driver.BoltDriver</li>
     *     <li>org.neo4j.ogm.drivers.http.driver.HttpDriver</li>
     *     <li>org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver</li>
     * </ul>
     */
    private String driver;

    /**
     * The uri of where the Neo4j server is: <br/>
     * <ul>
     *     <li>bolt://localhost:7687</li>
     *     <li>http://localhost:7474</li>
     *     <li>file:///var/tmp/neo4j.db</li>
     * </ul>
     */
    private String uri;

    /**
     * The username for connections that require authentication.
     */
    private String username;

    /**
     * The password for connections that require authentication.
     */
    private String password;

    /**
     * The maximum number of sessions per URL. This property is optional and defaults to 50.
     */
    private Integer connectionPoolSize;

    /**
     * Encryption level (TLS), optional.
     */
    private String encryptionLevel;

    /**
     * Trust strategy, optional, not used if not specified.
     */
    private String trustStrategy;

    /**
     * Trust certificate file, required if trust.strategy is specified
     */
    private String trustCertificateFile;

    /**
     * Enables the creation of things like predefined indexes on every start up
     */
    private boolean autoSchemaCreationOnStartUpEnabled = true;

    /**
     * Timeout in milliseconds before giving up opening connection on start up
     */
    private int autoSchemaCreationOnStartUpTimeout = 5000;

}
