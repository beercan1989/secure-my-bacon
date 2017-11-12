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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.co.baconi.secure.api.ApiApplication;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.bag.BagGraphRepository;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.AsymmetricLockGraphRepository;
import uk.co.baconi.secure.base.password.EncryptedPassword;
import uk.co.baconi.secure.base.password.PasswordService;
import uk.co.baconi.secure.base.user.User;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import java.util.stream.IntStream;

import static uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher.AES_CBC_PKCS7;

@RequiredArgsConstructor(onConstructor=@__({@Autowired}))
public class IntegrationApiApplication extends ApiApplication {

    private final BagGraphRepository bagGraphRepository;
    private final UserGraphRepository userRepository;
    private final AsymmetricLockGraphRepository asymmetricLockRepository;
    private final PasswordService passwordService;

    public void createTestData() {
        IntStream.range(0, 5).forEach(id -> {
            final Bag bag = createBag(bagGraphRepository, id);
            final User user = createUser(userRepository, id);

            createAsymmetricLock(asymmetricLockRepository, bag, user, id);

            createPassword(passwordService, bag, id);
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

    private static EncryptedPassword createPassword(final PasswordService passwordService, final Bag bag, final int id) {
        try {
            return passwordService.createAndShare(bag, "whereFor-" + id, "username-" + id, "password-" + id, AES_CBC_PKCS7, 128);
        } catch (final EncryptionException e) {
            throw new RuntimeException(e);
        }
    }

    @RestController
    public static class IntegrationTestController {

        @RequestMapping(value = "/fake/throw-error", method = RequestMethod.GET)
        public void testEndpointThatThrowsAnException() throws FakeException {
            throw new FakeException("integration-test-only-error");
        }

        private static class FakeException extends Exception {
            FakeException(final String message) {
                super(message);
            }
        }
    }
}
