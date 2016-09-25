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

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.baconi.secure.base.BaseIntegrationTest;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.user.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AsymmetricGraphRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private AsymmetricLockGraphRepository asymmetricLockGraphRepository;

    @Test
    public void shouldBeAbleToCreateAsymmetricLock() {
        final User user = new User("username");
        final Bag bag = new Bag("shouldBeAbleToCreateAsymmetricLockBag", "public key".getBytes());
        final byte[] privateKey = "private key".getBytes();

        final AsymmetricLock lock = new AsymmetricLock(bag, user, privateKey);

        final AsymmetricLock saved = asymmetricLockGraphRepository.save(lock);

        assertThat(saved, is(equalTo(lock)));
        assertThat(saved.getId(), is(not(nullValue())));
        assertThat(saved.getPrivateKey(), is(equalTo(privateKey)));
        assertThat(saved.getUser(), is(equalTo(user)));
        assertThat(saved.getBag(), is(equalTo(bag)));

        final AsymmetricLock one = asymmetricLockGraphRepository.findOne(lock.getId());

        assertThat(one, is(equalTo(lock)));
        assertThat(one.getId(), is(equalTo(lock.getId())));
        assertThat(one.getPrivateKey(), is(equalTo(privateKey)));
        assertThat(one.getUser(), is(equalTo(user)));
        assertThat(one.getBag(), is(equalTo(bag)));
    }

}
