/*
 * Copyright 2017 James Bacon
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

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.baconi.secure.base.bag.BagGraphRepository;
import uk.co.baconi.secure.base.lock.AsymmetricLockGraphRepository;
import uk.co.baconi.secure.base.lock.SymmetricLockGraphRepository;
import uk.co.baconi.secure.base.password.PasswordGraphRepository;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import java.io.Serializable;

@Slf4j
@RunWith(SpringRunner.class)
public abstract class TearDownRepositories {

    @Autowired
    private BagGraphRepository bagGraphRepository;

    @Autowired
    private AsymmetricLockGraphRepository asymmetricLockGraphRepository;

    @Autowired
    private SymmetricLockGraphRepository symmetricLockGraphRepository;

    @Autowired
    private PasswordGraphRepository passwordGraphRepository;

    @Autowired
    private UserGraphRepository userGraphRepository;

    @Before
    public void tearDownRepositories() {
        tearDownRepository(bagGraphRepository);
        tearDownRepository(asymmetricLockGraphRepository);
        tearDownRepository(symmetricLockGraphRepository);
        tearDownRepository(passwordGraphRepository);
        tearDownRepository(userGraphRepository);
    }

    private <A, B extends Serializable> void tearDownRepository(final CrudRepository<A, B> repository) {
        log.info("Tearing down repository: {}", repository.getClass());
        repository.deleteAll();
    }
}
