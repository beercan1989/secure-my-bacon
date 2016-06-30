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

package uk.co.baconi.secure.api.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.baconi.secure.base.lock.AsymmetricLock;
import uk.co.baconi.secure.base.lock.AsymmetricLockGraphRepository;
import uk.co.baconi.secure.base.pagination.PaginatedResult;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/asymmetric-locks", produces = "application/json; charset=UTF-8")
public class AsymmetricLockEndpoint {

    private final AsymmetricLockGraphRepository asymmetricLockGraphRepository;

    @Autowired
    public AsymmetricLockEndpoint(final AsymmetricLockGraphRepository asymmetricLockGraphRepository) {
        this.asymmetricLockGraphRepository = asymmetricLockGraphRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PaginatedResult<AsymmetricLock>> findAllPaginated(
            @Min(value = 0, message = "{uk.co.baconi.secure.api.Page.min}")
            @RequestParam(required = false, defaultValue = "0") final Integer page,

            @Min(value = 1, message = "{uk.co.baconi.secure.api.PerPage.min}")
            @Max(value = 20, message = "{uk.co.baconi.secure.api.PerPage.max}")
            @RequestParam(required = false, defaultValue = "5") final Integer perPage
    ) {

        final Page<AsymmetricLock> paged = asymmetricLockGraphRepository.findAll(new PageRequest(page, perPage));

        log.trace("paged: {}", paged);

        final PaginatedResult<AsymmetricLock> paginatedResult = new PaginatedResult<>(paged);

        log.trace("paginatedResult: {}", paginatedResult);

        return ResponseEntity.ok(paginatedResult);
    }

}
