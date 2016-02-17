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
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.IOException;

public class IsJson<T> extends TypeSafeMatcher<T> {
    private final Matcher<? super ReadContext> jsonMatcher;

    public IsJson(Matcher<? super ReadContext> jsonMatcher) {
        this.jsonMatcher = jsonMatcher;
    }

    @Override
    protected boolean matchesSafely(T json) {
        try {
            ReadContext context = parse(json);
            return jsonMatcher.matches(context);
        } catch (JsonPathException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void describeTo(Description description) {
        description.appendText("is json ").appendDescriptionOf(jsonMatcher);
    }

    @Override
    protected void describeMismatchSafely(T json, Description mismatchDescription) {
        try {
            ReadContext context = parse(json);
            jsonMatcher.describeMismatch(context, mismatchDescription);
        } catch (JsonPathException | IOException e) {
            buildMismatchDescription(json, mismatchDescription, e);
        }
    }

    private static void buildMismatchDescription(Object json, Description mismatchDescription, Exception e) {
        mismatchDescription
                .appendText("was ")
                .appendValue(json)
                .appendText(" which failed with ")
                .appendValue(e.getMessage());
    }

    private static ReadContext parse(Object object) throws IOException {
        if (object instanceof String) {
            return JsonPath.parse((String) object);
        } else if (object instanceof File) {
            return JsonPath.parse((File) object);
        } else {
            return JsonPath.parse(object);
        }
    }
}
