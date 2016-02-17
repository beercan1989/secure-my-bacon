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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.co.baconi.secure.base.bag.Bag;
import uk.co.baconi.secure.base.bag.BagGraphRepository;
import uk.co.baconi.secure.base.pagination.PaginatedResult;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/bags", produces = "application/json; charset=UTF-8")
public class BagEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(BagEndpoint.class);

    @Autowired
    private BagGraphRepository bagGraphRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PaginatedResult<Bag>> get(
            @Min(value = 0, message = "{uk.co.baconi.secure.api.Page.min}")
            @RequestParam(required = false, defaultValue = "0") final Integer page,

            @Min(value = 1, message = "{uk.co.baconi.secure.api.PerPage.min}")
            @Max(value = 20, message = "{uk.co.baconi.secure.api.PerPage.max}")
            @RequestParam(required = false, defaultValue = "5") final Integer perPage) {

        final Page<Bag> paged = bagGraphRepository.findAll(new PageRequest(page, perPage));

        LOG.trace("paged: {}", paged);

        final PaginatedResult<Bag> paginatedResult = new PaginatedResult<>(paged);

        LOG.trace("paginatedResult: {}", paginatedResult);

        return ResponseEntity.
                ok().
                body(paginatedResult);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> handleResourceNotFoundException(final ConstraintViolationException exception) {

        final List<String> violations = exception.
                getConstraintViolations().
                stream().
                map(ConstraintViolation::getMessage).
                collect(Collectors.toList());

        return ResponseEntity.
                badRequest().
                body(violations);
    }
}
