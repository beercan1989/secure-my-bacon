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

package uk.co.baconi.secure.base.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class PaginatedResult<A> {

    private final List<A> data;
    private final Pagination paging;

    public PaginatedResult(final Page<A> page) {
        this(page.getContent(), new Pagination(page.getNumber(), page.getSize(), page.getTotalElements()));
    }

    public PaginatedResult(final List<A> data) {
        this(data, new Pagination(0, data.size(), data.size()));
    }
}
