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

package uk.co.baconi.secure.api.password;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.bag.BagGraphRepository;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricEngine;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricGenerator;
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLockGraphRepository;
import uk.co.baconi.secure.base.pagination.PaginatedResult;
import uk.co.baconi.secure.base.password.Password;
import uk.co.baconi.secure.base.password.PasswordGraphRepository;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
@Validated
@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping(value = "/passwords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PasswordEndpoint {

    private final PasswordGraphRepository passwordGraphRepository;
    private final BagGraphRepository bagGraphRepository;
    private final SymmetricLockGraphRepository symmetricLockGraphRepository;

    private final SymmetricGenerator symmetricGenerator;
    private final SymmetricEngine symmetricEngine;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PaginatedResult<Password>> findAll(
            @Min(value = 0, message = "{uk.co.baconi.secure.api.Page.min}")
            @RequestParam(required = false, defaultValue = "0") final Integer page,

            @Min(value = 1, message = "{uk.co.baconi.secure.api.PerPage.min}")
            @Max(value = 20, message = "{uk.co.baconi.secure.api.PerPage.max}")
            @RequestParam(required = false, defaultValue = "5") final Integer perPage
    ) {

        final Page<Password> paged = passwordGraphRepository.findAll(new PageRequest(page, perPage));

        log.trace("paged: {}", paged);

        final PaginatedResult<Password> paginatedResult = new PaginatedResult<>(paged);

        log.trace("paginatedResult: {}", paginatedResult);

        return ResponseEntity.ok(paginatedResult);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Password> create(@Valid @RequestBody final NewPassword newPassword) throws EncryptionException {

        log.trace("createPassword: {}", newPassword);

        // Find user who this bag will initially belong to
        final Bag bag = bagGraphRepository.findByName(newPassword.getBag().getName());
        log.trace("foundBag: {}", bag);

        // TODO - Verify user has access to the bag

        final SymmetricCipher symmetricType = SymmetricCipher.AES_CBC_PKCS7;
        final int keySize = 256;

        final SecretKey symmetricKey = symmetricGenerator.generateKey(symmetricType, keySize);
        final AlgorithmParameterSpec parameterSpec = symmetricGenerator.generateParameters(symmetricType);

        final byte[] pwd = symmetricEngine.encrypt(symmetricType, symmetricKey, parameterSpec, newPassword.getPassword().getPassword());
        log.trace("encryptedPassword: [REDACTED]");

        final String whereFor = newPassword.getPassword().getWhereFor();
        final String username = newPassword.getPassword().getUsername();
        final Password password = passwordGraphRepository.save(new Password(whereFor, username, pwd));
        log.trace("createdPassword: {}", password);

        // TODO - Secure symmetricKey with bag.publicKey
        final SymmetricLock lock = symmetricLockGraphRepository.save(new SymmetricLock(password, bag, symmetricKey.getEncoded(), symmetricType));
        log.trace("createdSymmetricLock: {}", lock);

        // TODO - Make sure the SecretKey is no longer stored in memory
        // symmetricKey.destroy();

        // TODO - Review exposing encrypted passwords
        return ResponseEntity.ok(password);
    }
}
