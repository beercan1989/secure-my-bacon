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
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class WithJsonPath<T> extends TypeSafeMatcher<ReadContext> {
    private final JsonPath jsonPath;
    private final Matcher<T> resultMatcher;

    public WithJsonPath(JsonPath jsonPath, Matcher<T> resultMatcher) {
        this.jsonPath = jsonPath;
        this.resultMatcher = resultMatcher;
    }

    @Override
    protected boolean matchesSafely(ReadContext context) {
        try {
            T value = context.read(jsonPath);
            return resultMatcher.matches(value);
        } catch (JsonPathException e) {
            return false;
        }
    }

    public void describeTo(Description description) {
        description
                .appendText("with json path ")
                .appendValue(jsonPath.getPath())
                .appendText(" evaluated to ")
                .appendDescriptionOf(resultMatcher);
    }

    @Override
    protected void describeMismatchSafely(ReadContext context, Description mismatchDescription) {
        try {
            T value = jsonPath.read((Object) context.json());
            mismatchDescription
                    .appendText("json path ")
                    .appendValue(jsonPath.getPath())
                    .appendText(" was evaluated to ")
                    .appendValue(value);
        } catch (PathNotFoundException e) {
            mismatchDescription
                    .appendText("json path ")
                    .appendValue(jsonPath.getPath())
                    .appendText(" was not found in ")
                    .appendValue(context.json());
        } catch (JsonPathException e) {
            mismatchDescription
                    .appendText("was ")
                    .appendValue(context.json());
        }
    }

}
