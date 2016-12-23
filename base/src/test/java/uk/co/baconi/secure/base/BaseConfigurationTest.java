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

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.neo4j.ogm.config.DriverConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class BaseConfigurationTest extends BaseUnitTest {

    @Test
    public void shouldNotSetDriverConfigWhenNotProvided() {

        final Neo4JProperties neo4JProperties = mock(Neo4JProperties.class);
        final BaseConfiguration baseConfiguration = new BaseConfiguration(neo4JProperties);

        verifyZeroInteractions(neo4JProperties);

        when(neo4JProperties.getConnectionPoolSize()).thenReturn(null);

        final DriverConfiguration driverConfig = baseConfiguration.getNeo4jConfiguration().driverConfiguration();

        verify(neo4JProperties).getDriver();
        verify(neo4JProperties).getUsername();
        verify(neo4JProperties).getUrl();
        verify(neo4JProperties).getConnectionPoolSize();
        verify(neo4JProperties).getEncryptionLevel();
        verify(neo4JProperties).getTrustStrategy();
        verifyNoMoreInteractions(neo4JProperties);

        assertThat(driverConfig.getDriverClassName(), is(nullValue()));
        assertThat(driverConfig.getCredentials(), is(nullValue()));
        assertThat(driverConfig.getURI(), is(nullValue()));
        assertThat(driverConfig.getEncryptionLevel(), is(nullValue()));
        assertThat(driverConfig.getTrustStrategy(), is(nullValue()));
        assertThat(driverConfig.getTrustCertFile(), is(nullValue()));

    }

    @Test
    public void shouldSetDriverConfigWhenProvided() {

        final Neo4JProperties neo4JProperties = mock(Neo4JProperties.class);
        final BaseConfiguration baseConfiguration = new BaseConfiguration(neo4JProperties);

        verifyZeroInteractions(neo4JProperties);

        when(neo4JProperties.getDriver()).thenReturn("custom.driver");
        when(neo4JProperties.getUsername()).thenReturn("custom.user");
        when(neo4JProperties.getPassword()).thenReturn("custom.password");
        when(neo4JProperties.getUrl()).thenReturn("http://custom.url");
        when(neo4JProperties.getConnectionPoolSize()).thenReturn(666);
        when(neo4JProperties.getEncryptionLevel()).thenReturn("custom.encryption.level");
        when(neo4JProperties.getTrustStrategy()).thenReturn("custom.trust.nothing");
        when(neo4JProperties.getTrustCertificateFile()).thenReturn("custom.trust.file");

        final DriverConfiguration driverConfig = baseConfiguration.getNeo4jConfiguration().driverConfiguration();

        verify(neo4JProperties, times(2)).getDriver();
        verify(neo4JProperties, times(2)).getUsername();
        verify(neo4JProperties, times(2)).getPassword();
        verify(neo4JProperties, times(2)).getUrl();
        verify(neo4JProperties, times(2)).getConnectionPoolSize();
        verify(neo4JProperties, times(2)).getEncryptionLevel();
        verify(neo4JProperties, times(2)).getTrustStrategy();
        verify(neo4JProperties, times(2)).getTrustCertificateFile();
        verifyNoMoreInteractions(neo4JProperties);

        assertThat(driverConfig.getDriverClassName(), is(equalTo("custom.driver")));
        assertThat(driverConfig.getCredentials().credentials(), is(equalTo(Base64.encodeBase64String("custom.user".concat(":").concat("custom.password").getBytes()))));
        assertThat(driverConfig.getURI(), is(equalTo("http://custom.url")));
        assertThat(driverConfig.getConnectionPoolSize(), is(equalTo(666)));
        assertThat(driverConfig.getEncryptionLevel(), is(equalTo("custom.encryption.level")));
        assertThat(driverConfig.getTrustStrategy(), is(equalTo("custom.trust.nothing")));
        assertThat(driverConfig.getTrustCertFile(), is(equalTo("custom.trust.file")));
    }
}
