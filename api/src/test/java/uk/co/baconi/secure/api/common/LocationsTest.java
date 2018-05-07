/*
 * Copyright 2018 James Bacon
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

package uk.co.baconi.secure.api.common;

import org.junit.Test;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationsTest {

    @Test
    public void shouldBeAbleToCreateUriForPasswordByUuid() {

        final UUID uuid = UUID.randomUUID();
        final URI uri = Locations.passwordByUuid(uuid);

        assertThat(uri)
                .hasPath("/passwords/by-uuid/" + uuid.toString())
                .hasNoFragment()
                .hasNoParameters()
                .hasNoPort()
                .hasNoQuery()
                .hasNoUserInfo();
    }

    @Test
    public void shouldBeAbleToCreateUriForUserByName() {

        final URI uri = Locations.userByName("bob");

        assertThat(uri)
                .hasPath("/users/by-name/bob")
                .hasNoFragment()
                .hasNoParameters()
                .hasNoPort()
                .hasNoQuery()
                .hasNoUserInfo();
    }

    @Test
    public void shouldBeAbleToCreateUriForBagByName() {

        final URI uri = Locations.bagByName("sports");

        assertThat(uri)
                .hasPath("/bags/by-name/sports")
                .hasNoFragment()
                .hasNoParameters()
                .hasNoPort()
                .hasNoQuery()
                .hasNoUserInfo();
    }
}
