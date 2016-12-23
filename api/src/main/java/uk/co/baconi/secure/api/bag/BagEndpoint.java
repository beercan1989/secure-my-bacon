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

package uk.co.baconi.secure.api.bag;

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
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.AsymmetricLockGraphRepository;
import uk.co.baconi.secure.base.pagination.PaginatedResult;
import uk.co.baconi.secure.base.user.User;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping(value = "/bags", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BagEndpoint {

    private final BagGraphRepository bagGraphRepository;
    private final UserGraphRepository userGraphRepository;
    private final AsymmetricLockGraphRepository asymmetricLockGraphRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PaginatedResult<Bag>> findAll(
            @Min(value = 0, message = "{uk.co.baconi.secure.api.Page.min}")
            @RequestParam(required = false, defaultValue = "0") final Integer page,

            @Min(value = 1, message = "{uk.co.baconi.secure.api.PerPage.min}")
            @Max(value = 20, message = "{uk.co.baconi.secure.api.PerPage.max}")
            @RequestParam(required = false, defaultValue = "5") final Integer perPage
    ) {

        final Page<Bag> paged = bagGraphRepository.findAll(new PageRequest(page, perPage));

        log.trace("paged: {}", paged);

        final PaginatedResult<Bag> paginatedResult = new PaginatedResult<>(paged);

        log.trace("paginatedResult: {}", paginatedResult);

        return ResponseEntity.ok(paginatedResult);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Bag> create(@Valid @RequestBody final NewBag newBag) {

        log.trace("createBag: {}", newBag);

        // TODO - Verify user exists
        // Find user who this bag will initially belong to
        final User user = userGraphRepository.findByName(newBag.getUserName());
        log.trace("foundUser: {}", user);

        // TODO - Generate bags key pair
        final byte[] publicKey = {1, 2, 3, 4};
        final byte[] privateKey = {5, 6, 7, 8};

        final Bag bag = bagGraphRepository.save(new Bag(newBag.getBagName(), publicKey));
        log.trace("createdBag: {}", bag);

        // TODO - Secure private key with User details
        final AsymmetricLock lock = asymmetricLockGraphRepository.save(new AsymmetricLock(bag, user, privateKey));
        log.trace("createdAsymmetricLock: {}", lock);

        return ResponseEntity.ok(bag);
    }

    @RequestMapping(value = "/by-name/{bag-name}", method = RequestMethod.GET)
    public ResponseEntity<Bag> findByName(@PathVariable("bag-name") final String name) throws NotFoundException {

        log.trace("findByName: {}", name);

        final Bag bag = bagGraphRepository.findByName(name);

        log.trace("foundBag: {}", bag);

        if (bag == null) {
            throw NotFoundException.bagByName(name);
        } else {
            return ResponseEntity.ok(bag);
        }
    }
}
