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

package uk.co.baconi.secure.base.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.baconi.secure.base.BaseIntegrationTest;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.lock.AsymmetricLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserGraphRepositoryIT extends BaseIntegrationTest {

    @Autowired
    private UserGraphRepository userGraphRepository;

    @Test
    public void shouldBeAbleToCreateAndRetrieveUser() {
        final String name = "shouldBeAbleToCreateAndRetrieveUser";

        final User user = new User(name);

        final User saved = userGraphRepository.save(user);

        assertThat(saved, is(equalTo(user)));
        assertThat(saved.getId(), is(not(nullValue())));
        assertThat(saved.getName(), is(equalTo(name)));
        assertThat(saved.getShared(), is(empty()));

        final User one = userGraphRepository.findOne(user.getId());

        assertThat(one, is(equalTo(saved)));
        assertThat(one.getId(), is(equalTo(user.getId())));
        assertThat(one.getName(), is(equalTo(name)));
        assertThat(one.getShared(), is(empty()));
    }

    @Test
    public void shouldBeAbleToShareUserWithAGroup() {
        final String name = "shouldBeAbleToShareUserWithAGroup_User";

        final User user = new User(name);

        userGraphRepository.save(user);

        final Bag gitHubBag = new Bag("shouldBeAbleToShareUserWithAGroup_Group", "public key".getBytes());

        final AsymmetricLock sharedWith = new AsymmetricLock(gitHubBag, user, "privateKey".getBytes());

        assertThat(user.getShared(), contains(sharedWith));

        assertThat(gitHubBag.getId(), is(nullValue()));
        assertThat(sharedWith.getId(), is(nullValue()));

        userGraphRepository.save(user);

        assertThat(user.getId(), is(not(nullValue())));
        assertThat(gitHubBag.getId(), is(not(nullValue())));
        assertThat(sharedWith.getId(), is(not(nullValue())));
    }

}
