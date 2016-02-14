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

package uk.co.baconi.secure.base.bag;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.baconi.secure.base.BaseConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseConfiguration.class, loader = SpringApplicationContextLoader.class)
public class BagGraphRepositoryIT {

    // TODO - Review embbedded server once Spring Data Neo4j 4.1 released: http://docs.spring.io/spring-data/data-neo4j/docs/4.1.0.M1/reference/html/#_drivers

    @Autowired
    private BagGraphRepository bagGraphRepository;

    @Test
    public void shouldBeAbleToCreateAndRetrieveBag() {
        final String name = "Substeps";
        final byte[] publicKey = "public key".getBytes();

        final Bag bag = new Bag(name, publicKey);

        final Bag saved = bagGraphRepository.save(bag);

        assertThat(saved, Matchers.is(equalTo(bag)));
        assertThat(saved.getId(), is(not(nullValue())));
        assertThat(saved.getName(), is(equalTo(name)));
        assertThat(saved.getPublicKey(), is(equalTo(publicKey)));
        assertThat(saved.getSecured(), is(empty()));
        assertThat(saved.getShared(), is(empty()));

        final Bag one = bagGraphRepository.findOne(bag.getId());

        assertThat(one, Matchers.is(equalTo(saved)));
        assertThat(one.getId(), is(not(nullValue())));
        assertThat(one.getName(), is(equalTo(name)));
        assertThat(one.getPublicKey(), is(equalTo(publicKey)));
        assertThat(one.getSecured(), is(empty()));
        assertThat(one.getShared(), is(empty()));
    }

}
