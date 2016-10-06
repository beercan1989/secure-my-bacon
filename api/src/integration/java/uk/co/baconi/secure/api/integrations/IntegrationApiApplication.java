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

package uk.co.baconi.secure.api.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.co.baconi.secure.api.ApiApplication;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.bag.BagGraphRepository;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.AsymmetricLockGraphRepository;
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLockGraphRepository;
import uk.co.baconi.secure.base.password.Password;
import uk.co.baconi.secure.base.password.PasswordGraphRepository;
import uk.co.baconi.secure.base.user.User;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import java.util.stream.IntStream;

public class IntegrationApiApplication extends ApiApplication {

    @Autowired
    public IntegrationApiApplication(
            final BagGraphRepository bagGraphRepository,
            final UserGraphRepository userRepository,
            final AsymmetricLockGraphRepository asymmetricLockRepository,
            final PasswordGraphRepository passwordRepository,
            final SymmetricLockGraphRepository symmetricLockRepository
    ) {
        IntStream.range(0, 5).forEach(id -> {
            final Bag bag = createBag(bagGraphRepository, id);
            final User user = createUser(userRepository, id);

            createAsymmetricLock(asymmetricLockRepository, bag, user, id);

            final Password password = createPassword(passwordRepository, id);

            createSymmetricLock(symmetricLockRepository, password, bag, id);
        });
    }

    private static User createUser(final UserGraphRepository repository, final int id) {
        return repository.save(new User("user-" + id));
    }

    private static Bag createBag(final BagGraphRepository repository, final int id) {
        return repository.save(new Bag("bag-" + id, ("public-key-" + id).getBytes()));
    }

    private static AsymmetricLock createAsymmetricLock(final AsymmetricLockGraphRepository repository, final Bag bag, final User user, final int id) {
        return repository.save(new AsymmetricLock(bag, user, ("private-key-" + id).getBytes()));
    }

    private static Password createPassword(final PasswordGraphRepository repository, final int id) {
        return repository.save(new Password("whereFor-" + id, "username-" + id, "password-" + id));
    }

    private static SymmetricLock createSymmetricLock(final SymmetricLockGraphRepository repository, final Password password, final Bag bag, final int id) {
        return repository.save(new SymmetricLock(password, bag, ("key-" + id).getBytes()));
    }

    @RestController
    public static class IntegrationTestController {

        @RequestMapping(value = "/fake/throw-error", method = RequestMethod.GET)
        public void testEndpointThatThrowsAnException() throws Exception {
            throw new Exception("integration-test-only-error");
        }
    }
}
