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
import uk.co.baconi.secure.api.exceptions.NotFoundException;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.bag.BagGraphRepository;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;
import uk.co.baconi.secure.base.pagination.PaginatedResult;
import uk.co.baconi.secure.base.password.EncryptedPassword;
import uk.co.baconi.secure.base.password.PasswordGraphRepository;
import uk.co.baconi.secure.base.password.PasswordService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static uk.co.baconi.secure.api.common.Locations.*;

@Slf4j
@Validated
@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping(value = PASSWORDS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PasswordEndpoint {

    private final PasswordGraphRepository passwordGraphRepository;
    private final BagGraphRepository bagGraphRepository;

    private final PasswordService passwordService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PaginatedResult<EncryptedPassword>> findAll(
            @Min(value = 0, message = "{uk.co.baconi.secure.api.Page.min}")
            @RequestParam(required = false, defaultValue = "0") final Integer page,

            @Min(value = 1, message = "{uk.co.baconi.secure.api.PerPage.min}")
            @Max(value = 20, message = "{uk.co.baconi.secure.api.PerPage.max}")
            @RequestParam(required = false, defaultValue = "5") final Integer perPage
    ) {

        log.trace("findAll: {}, {}", page, perPage);

        final Page<EncryptedPassword> paged = passwordGraphRepository.findAll(new PageRequest(page, perPage));

        log.trace("paged: {}", paged);

        final PaginatedResult<EncryptedPassword> paginatedResult = new PaginatedResult<>(paged);

        log.trace("paginatedResult: {}", paginatedResult);

        return ResponseEntity.ok(paginatedResult);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<NewPasswordResponse> create(@Valid @RequestBody final NewPassword newPassword) throws EncryptionException {

        log.trace("Create a {}", newPassword);

        // Find user who this bag will initially belong to
        final Bag bag = bagGraphRepository.findByName(newPassword.getBag().getName());

        log.trace("Found a {}", bag);

        // TODO - Verify user has access to the bag, as USer might not be able to access the password if they do not.

        final String whereFor = newPassword.getPassword().getWhereFor();
        final String username = newPassword.getPassword().getUsername();
        final String rawPassword = newPassword.getPassword().getPassword();

        // TODO - Extract into incoming request.
        final SymmetricCipher symmetricType = SymmetricCipher.AES_CBC_PKCS7;
        final int keySize = 256;

        final EncryptedPassword password = passwordService.createAndShare(bag, whereFor, username, rawPassword, symmetricType, keySize);

        log.trace("Created and shared {} with {}", password, bag);

        final NewPasswordResponse response = new NewPasswordResponse(password);
        final URI location = passwordByUuid(response.getUuid());

        log.trace("Responding with {} and location {}", response, location);

        return ResponseEntity.created(location).body(response);
    }

    @RequestMapping(value = FOR_USER + "{user-name}", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResult<EncryptedPassword>> getPasswordsForUser(
            @Min(value = 0, message = "{uk.co.baconi.secure.api.Page.min}")
            @RequestParam(required = false, defaultValue = "0") final Integer page,

            @Min(value = 1, message = "{uk.co.baconi.secure.api.PerPage.min}")
            @Max(value = 20, message = "{uk.co.baconi.secure.api.PerPage.max}")
            @RequestParam(required = false, defaultValue = "5") final Integer perPage,

            @PathVariable("user-name") final String name
    ) {

        log.trace("getPasswordsForUser: {}, {}, {}", name, page, perPage);

        final List<EncryptedPassword> fullPage = passwordGraphRepository.getPasswordsForUser(name);

        log.trace("fullPage: {}", fullPage);

        final PaginatedResult<EncryptedPassword> paginatedResult = new PaginatedResult<>(fullPage);

        log.trace("paginatedResult: {}", paginatedResult);

        return ResponseEntity.ok(paginatedResult);
    }

    @RequestMapping(value = BY_UUID + "{password-uuid}", method = RequestMethod.GET)
    public ResponseEntity<EncryptedPassword> getPasswordByUuid(@PathVariable("password-uuid") final UUID uuid) throws NotFoundException {

        log.trace("getPasswordByUuid: {}", uuid);

        final EncryptedPassword password = passwordGraphRepository.findByUuid(uuid);

        log.trace("foundPassword: {}", password);

        if (password == null) {
            throw NotFoundException.passwordByUuid(uuid);
        } else {
            return ResponseEntity.ok(password);
        }
    }

    @RequestMapping(value = BY_UUID + "{password-uuid}"+ FOR_USER + "{user-name}", method = RequestMethod.GET)
    public ResponseEntity<EncryptedPassword> getPasswordForUser(@PathVariable("password-uuid") final UUID passwordUuid,
                                                                @PathVariable("user-name") final String userName) throws NotFoundException {

        log.trace("getPasswordForUser: {}, {}", passwordUuid, userName);

        final EncryptedPassword password = passwordGraphRepository.getPasswordForUser(passwordUuid, userName);

        log.trace("foundPassword: {}", password);

        if (password == null) {
            throw NotFoundException.passwordByUuidForUser(passwordUuid, userName);
        } else {
            return ResponseEntity.ok(password);
        }
    }
}
