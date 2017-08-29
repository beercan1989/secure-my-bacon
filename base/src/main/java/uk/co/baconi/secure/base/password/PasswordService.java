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
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricEngine;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricGenerator;
import uk.co.baconi.secure.base.lock.SymmetricLock;

import javax.crypto.SecretKey;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
@Service
@Validated
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class PasswordService {

    private final SymmetricGenerator symmetricGenerator;
    private final SymmetricEngine symmetricEngine;

    private final PasswordGraphRepository passwordGraphRepository;

    /**
     * Create a new Password encrypted and shared with the provided Bag.
     *
     * @param bag the Bag to initially share the Password with
     * @param whereFor
     * @param username
     * @param rawPassword
     * @param cipherType
     * @param keySize
     *
     * @return the secured and saved Password
     *
     * @throws EncryptionException is thrown if there has been any issues with encrypting the provided data.
     */
    public Password createAndShare(
            @NotNull(message = "{uk.co.baconi.secure.base.Bag.not.null}")
            final Bag bag,

            final String whereFor,
            final String username,

            @NotBlank(message = "{uk.co.baconi.secure.base.Password.populated}")
            final String rawPassword,

            @NotNull(message = "{uk.co.baconi.secure.base.SymmetricCipher.not.null}")
            final SymmetricCipher cipherType,

            @Min(value = 1, message = "{uk.co.baconi.secure.base.KeySize.not.negative}")
            final Integer keySize
    ) throws EncryptionException {

        final SecretKey symmetricKey = symmetricGenerator.generateKey(cipherType, keySize);
        final AlgorithmParameterSpec parameterSpec = symmetricGenerator.generateParameters(cipherType);

        final byte[] encryptedPassword = symmetricEngine.encrypt(cipherType, symmetricKey, parameterSpec, rawPassword);

        final Password password = new Password(whereFor, username, encryptedPassword);

        // TODO - Encrypt symmetric key with the bags's public key
        new SymmetricLock(password, bag, symmetricKey.getEncoded(), cipherType);

        // Save two deep to make sure the Bag in the SymmetricLock is also saved.
        return passwordGraphRepository.save(password, 2);
    }


}
