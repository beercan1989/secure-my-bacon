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

public class Pagination {

    private final int page;
    private final int perPage;
    private final long totalCount;

    public Pagination(final int page, final int perPage, final long totalCount) {
        this.page = page;
        this.perPage = perPage;
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", perPage=" + perPage +
                ", totalCount=" + totalCount +
                '}';
    }
}
