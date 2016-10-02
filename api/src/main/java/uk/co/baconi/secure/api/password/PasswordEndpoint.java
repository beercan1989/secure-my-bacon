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
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLockGraphRepository;
import uk.co.baconi.secure.base.pagination.PaginatedResult;
import uk.co.baconi.secure.base.password.Password;
import uk.co.baconi.secure.base.password.PasswordGraphRepository;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping(value = "/passwords", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PasswordEndpoint {

    private final PasswordGraphRepository passwordGraphRepository;
    private final BagGraphRepository bagGraphRepository;
    private final SymmetricLockGraphRepository symmetricLockGraphRepository;

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
    public ResponseEntity<Password> create(@Valid @RequestBody final NewPassword newPassword) {

        log.trace("createPassword: {}", newPassword);

        // Find user who this bag will initially belong to
        final Bag bag = bagGraphRepository.findByName(newPassword.getBag().getName());
        log.trace("foundBag: {}", bag);

        // TODO - Generate symmetric key
        final byte[] symmetricKey = {9, 10, 11, 12};

        final String whereFor = newPassword.getPassword().getWhereFor();
        final String username = newPassword.getPassword().getUsername();
        final String pwd = newPassword.getPassword().getPassword();

        // TODO - Secure 'pwd' with symmetric key
        final Password password = passwordGraphRepository.save(new Password(whereFor, username, pwd));
        log.trace("createdPassword: {}", password);

        final SymmetricLock lock = symmetricLockGraphRepository.save(new SymmetricLock(password, bag, symmetricKey));
        log.trace("createdSymmetricLock: {}", lock);

        return ResponseEntity.ok(password);
    }
}
