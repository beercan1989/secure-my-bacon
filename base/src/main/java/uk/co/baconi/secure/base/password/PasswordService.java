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

package uk.co.baconi.secure.base.password;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricEngine;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricGenerator;
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.user.User;

import javax.crypto.SecretKey;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.security.spec.AlgorithmParameterSpec;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class PasswordService {

    private final SymmetricGenerator symmetricGenerator;
    private final SymmetricEngine symmetricEngine;

    private final PasswordGraphRepository passwordGraphRepository;

    public Password create(
            @NotNull final User user,
            @NotNull final Bag bag,

            final String whereFor,
            final String username,
            @NotNull final String rawPassword,

            @NotNull final SymmetricCipher cipherType,
            @Min(128) final Integer keySize
    ) throws EncryptionException {

        // TODO - Verify user has access to the bag

        final SecretKey symmetricKey = symmetricGenerator.generateKey(cipherType, keySize);
        final AlgorithmParameterSpec parameterSpec = symmetricGenerator.generateParameters(cipherType);

        final byte[] encryptedPassword = symmetricEngine.encrypt(cipherType, symmetricKey, parameterSpec, rawPassword);

        final Password password = new Password(whereFor, username, encryptedPassword);

        // TODO - Encrypt key with the bags's public key
        new SymmetricLock(password, bag, symmetricKey.getEncoded(), cipherType);

        // Save two deep to make sure the Bag in the SymmetricLock is also saved.
        return passwordGraphRepository.save(password, 2);
    }

}
