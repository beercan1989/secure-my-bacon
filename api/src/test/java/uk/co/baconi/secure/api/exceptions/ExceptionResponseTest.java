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

package uk.co.baconi.secure.api.exceptions;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class ExceptionResponseTest {

    @Test
    public void shouldHaveNiceToStringRepresentation() {

        final ExceptionResponse exceptionResponse = new ExceptionResponse(mock(Exception.class));

        final String exceptionResponseAsString = exceptionResponse.toString();

        assertThat(exceptionResponseAsString, containsString("uuid="));
        assertThat(exceptionResponseAsString, containsString("name=$java.lang.Exception$$EnhancerByMockito"));
    }

    @Test
    public void shouldHaveUuidField() {

        final ExceptionResponse exceptionResponse = new ExceptionResponse(mock(Exception.class));

        assertThat(exceptionResponse.getUuid(), is(notNullValue()));
    }

    @Test
    public void shouldHaveNameField() {

        final ExceptionResponse exceptionResponse = new ExceptionResponse(mock(Exception.class));

        assertThat(exceptionResponse.getName(), containsString("$java.lang.Exception$$EnhancerByMockito"));
    }
}
