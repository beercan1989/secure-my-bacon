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

package uk.co.baconi.secure.base.lock;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.baconi.secure.base.BaseConfiguration;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.password.Password;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseConfiguration.class, loader = SpringApplicationContextLoader.class)
public class SymmetricGraphRepositoryIT {

    @Autowired
    private SymmetricLockGraphRepository symmetricLockGraphRepository;

    @Test
    public void shouldBeAbleToCreateLock() {
        final Password password = new Password("whereFor", "username", "password");
        final Bag bag = new Bag("bag", "public key".getBytes());
        final byte[] key = "key".getBytes();

        final SymmetricLock lock = new SymmetricLock(password, bag, key);

        final SymmetricLock saved = symmetricLockGraphRepository.save(lock);

        assertThat(saved, is(equalTo(lock)));
        assertThat(saved.getId(), is(not(nullValue())));
        assertThat(saved.getKey(), is(equalTo(key)));
        assertThat(saved.getPassword(), is(equalTo(password)));
        assertThat(saved.getBag(), is(equalTo(bag)));

        final SymmetricLock one = symmetricLockGraphRepository.findOne(lock.getId());

        assertThat(one, is(equalTo(lock)));
        assertThat(one.getId(), is(equalTo(lock.getId())));
        assertThat(one.getKey(), is(equalTo(key)));
        assertThat(one.getPassword(), is(equalTo(password)));
        assertThat(one.getBag(), is(equalTo(bag)));
    }

}
