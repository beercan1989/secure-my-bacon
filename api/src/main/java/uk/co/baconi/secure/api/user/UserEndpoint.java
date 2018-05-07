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

package uk.co.baconi.secure.api.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.co.baconi.secure.api.common.Locations;
import uk.co.baconi.secure.api.exceptions.NotFoundException;
import uk.co.baconi.secure.base.pagination.PaginatedResult;
import uk.co.baconi.secure.base.user.User;
import uk.co.baconi.secure.base.user.UserGraphRepository;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;

@Slf4j
@Validated
@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserEndpoint {

    private final UserGraphRepository userGraphRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PaginatedResult<User>> findAll(
            @Min(value = 0, message = "{uk.co.baconi.secure.api.Page.min}")
            @RequestParam(required = false, defaultValue = "0") final Integer page,

            @Min(value = 1, message = "{uk.co.baconi.secure.api.PerPage.min}")
            @Max(value = 20, message = "{uk.co.baconi.secure.api.PerPage.max}")
            @RequestParam(required = false, defaultValue = "5") final Integer perPage
    ) {

        final Page<User> paged = userGraphRepository.findAll(new PageRequest(page, perPage));

        log.trace("paged: {}", paged);

        final PaginatedResult<User> paginatedResult = new PaginatedResult<>(paged);

        log.trace("paginatedResult: {}", paginatedResult);

        return ResponseEntity.ok(paginatedResult);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody final NewUser newUser) {

        log.trace("createUser: {}", newUser);

        final User user = userGraphRepository.save(new User(newUser.getName()));

        log.trace("createdUser: {}", user);

        final URI location = Locations.userByName(user.getName());
        log.trace("Responding with {} and location {}", user, location);

        return ResponseEntity.created(location).body(user);
    }

    @RequestMapping(value = "/by-name/{user-name}", method = RequestMethod.GET)
    public ResponseEntity<User> findByName(@PathVariable("user-name") final String name) throws NotFoundException {

        log.trace("findByName: {}", name);

        final User user = userGraphRepository.findByName(name);

        log.trace("foundUser: {}", user);

        if (user == null) {
            throw NotFoundException.userByName(name);
        } else {
            return ResponseEntity.ok(user);
        }
    }
}
