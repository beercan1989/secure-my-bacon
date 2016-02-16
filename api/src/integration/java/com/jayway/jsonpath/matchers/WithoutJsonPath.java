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

package com.jayway.jsonpath.matchers;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.ReadContext;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.empty;

public class WithoutJsonPath extends TypeSafeDiagnosingMatcher<ReadContext> {
    private final JsonPath jsonPath;

    public WithoutJsonPath(JsonPath jsonPath) {
        this.jsonPath = jsonPath;
    }

    @Override
    protected boolean matchesSafely(ReadContext actual, Description mismatchDescription) {
        try {
            Object value = actual.read(jsonPath);
            mismatchDescription
                    .appendText(jsonPath.getPath())
                    .appendText(" was evaluated to ")
                    .appendValue(value);
            return value == null || empty().matches(value);
        } catch (JsonPathException e) {
            return true;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("without json path ").appendValue(jsonPath.getPath());
    }
}
