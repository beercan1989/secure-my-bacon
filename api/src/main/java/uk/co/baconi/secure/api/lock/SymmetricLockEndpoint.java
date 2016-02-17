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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.co.baconi.secure.base.lock.SymmetricLock;
import uk.co.baconi.secure.base.lock.SymmetricLockGraphRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/symmetric-locks", produces = "application/json; charset=UTF-8")
public class SymmetricLockEndpoint {

    @Autowired
    private SymmetricLockGraphRepository symmetricLockGraphRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<SymmetricLock>> get(){

        final List<SymmetricLock> allSymmetricLocks = StreamSupport.
                stream(symmetricLockGraphRepository.findAll().spliterator(), false).
                collect(Collectors.toList());

        return ResponseEntity.
                ok().
                body(allSymmetricLocks);
    }


}
