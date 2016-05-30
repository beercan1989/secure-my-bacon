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

        final BaseNeo4JProperties baseNeo4JProperties = mock(BaseNeo4JProperties.class);
        final BaseConfiguration baseConfiguration = new BaseConfiguration(baseNeo4JProperties);

        verifyZeroInteractions(baseNeo4JProperties);

        when(baseNeo4JProperties.getConnectionPoolSize()).thenReturn(null);

        final DriverConfiguration driverConfig = baseConfiguration.getNeo4jConfiguration().driverConfiguration();

        verify(baseNeo4JProperties).getDriver();
        verify(baseNeo4JProperties).getUsername();
        verify(baseNeo4JProperties).getUrl();
        verify(baseNeo4JProperties).getConnectionPoolSize();
        verify(baseNeo4JProperties).getEncryptionLevel();
        verify(baseNeo4JProperties).getTrustStrategy();
        verifyNoMoreInteractions(baseNeo4JProperties);

        assertThat(driverConfig.getDriverClassName(), is(nullValue()));
        assertThat(driverConfig.getCredentials(), is(nullValue()));
        assertThat(driverConfig.getURI(), is(nullValue()));
        assertThat(driverConfig.getEncryptionLevel(), is(nullValue()));
        assertThat(driverConfig.getTrustStrategy(), is(nullValue()));
        assertThat(driverConfig.getTrustCertFile(), is(nullValue()));

    }

    @Test
    public void shouldSetDriverConfigWhenProvided() {

        final BaseNeo4JProperties baseNeo4JProperties = mock(BaseNeo4JProperties.class);
        final BaseConfiguration baseConfiguration = new BaseConfiguration(baseNeo4JProperties);

        verifyZeroInteractions(baseNeo4JProperties);

        when(baseNeo4JProperties.getDriver()).thenReturn("custom.driver");
        when(baseNeo4JProperties.getUsername()).thenReturn("custom.user");
        when(baseNeo4JProperties.getPassword()).thenReturn("custom.password");
        when(baseNeo4JProperties.getUrl()).thenReturn("http://custom.url");
        when(baseNeo4JProperties.getConnectionPoolSize()).thenReturn(666);
        when(baseNeo4JProperties.getEncryptionLevel()).thenReturn("custom.encryption.level");
        when(baseNeo4JProperties.getTrustStrategy()).thenReturn("custom.trust.nothing");
        when(baseNeo4JProperties.getTrustCertificateFile()).thenReturn("custom.trust.file");

        final DriverConfiguration driverConfig = baseConfiguration.getNeo4jConfiguration().driverConfiguration();

        verify(baseNeo4JProperties, times(2)).getDriver();
        verify(baseNeo4JProperties, times(2)).getUsername();
        verify(baseNeo4JProperties, times(2)).getPassword();
        verify(baseNeo4JProperties, times(2)).getUrl();
        verify(baseNeo4JProperties, times(2)).getConnectionPoolSize();
        verify(baseNeo4JProperties, times(2)).getEncryptionLevel();
        verify(baseNeo4JProperties, times(2)).getTrustStrategy();
        verify(baseNeo4JProperties, times(2)).getTrustCertificateFile();
        verifyNoMoreInteractions(baseNeo4JProperties);

        assertThat(driverConfig.getDriverClassName(), is(equalTo("custom.driver")));
        assertThat(driverConfig.getCredentials().credentials(), is(equalTo(Base64.encodeBase64String("custom.user".concat(":").concat("custom.password").getBytes()))));
        assertThat(driverConfig.getURI(), is(equalTo("http://custom.url")));
        assertThat(driverConfig.getConnectionPoolSize(), is(equalTo(666)));
        assertThat(driverConfig.getEncryptionLevel(), is(equalTo("custom.encryption.level")));
        assertThat(driverConfig.getTrustStrategy(), is(equalTo("custom.trust.nothing")));
        assertThat(driverConfig.getTrustCertFile(), is(equalTo("custom.trust.file")));
    }
}
