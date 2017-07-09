/*
 * Copyright 2017 James Bacon
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

package uk.co.baconi.function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ThrowingFunctionTest {

    private final TestThrowingFunction underTest = mock(TestThrowingFunction.class, CALLS_REAL_METHODS);

    @Before
    public void before() throws Exception {
        reset(underTest);
    }

    @Test
    public void shouldSupportCallingAnotherFunction() throws Exception {
        final TestFunction function = mock(TestFunction.class, CALLS_REAL_METHODS);

        underTest.andThen(function).apply("test-calling-function");

        final InOrder inOrder = inOrder(underTest, function);
        inOrder.verify(underTest).apply("test-calling-function");
        inOrder.verify(function).apply("test-calling-function");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldSupportCallingAnotherThrowingFunction() throws Exception {
        final TestThrowingFunction throwingFunction = mock(TestThrowingFunction.class, CALLS_REAL_METHODS);

        underTest.andThen(throwingFunction).apply("test-calling-throwing-function");

        final InOrder inOrder = inOrder(underTest, throwingFunction);
        inOrder.verify(underTest).apply("test-calling-throwing-function");
        inOrder.verify(throwingFunction).apply("test-calling-throwing-function");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldStopProcessingChainIfExceptionThrown() throws Exception {
        doThrow(new Exception("test-thrown-exception")).when(underTest).apply(anyString());

        final TestThrowingFunction throwingFunction = mock(TestThrowingFunction.class, CALLS_REAL_METHODS);

        final ThrowingFunction<String, String, Exception> chainedUnderTest = underTest.andThen(throwingFunction);

        verifyProcessingStoppedOnceExceptionIsThrown(chainedUnderTest, throwingFunction);
    }

    @Test
    public void shouldSupportComposingWithAnotherFunction() throws Exception {
        final TestFunction function = mock(TestFunction.class, CALLS_REAL_METHODS);

        underTest.compose(function).apply("test-composing-function");

        final InOrder inOrder = inOrder(underTest, function);
        inOrder.verify(function).apply("test-composing-function");
        inOrder.verify(underTest).apply("test-composing-function");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldSupportComposingWithAnotherThrowingFunction() throws Exception {
        final TestThrowingFunction throwingFunction = mock(TestThrowingFunction.class, CALLS_REAL_METHODS);

        underTest.compose(throwingFunction).apply("test-composing-throwing-function");

        final InOrder inOrder = inOrder(underTest, throwingFunction);
        inOrder.verify(throwingFunction).apply("test-composing-throwing-function");
        inOrder.verify(underTest).apply("test-composing-throwing-function");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldStopProcessingComposedIfExceptionThrown() throws Exception {
        doThrow(new Exception("test-thrown-exception")).when(underTest).apply(anyString());

        final TestThrowingFunction throwingFunction = mock(TestThrowingFunction.class, CALLS_REAL_METHODS);

        final ThrowingFunction<String, String, Exception> composedUnderTest = underTest.compose(throwingFunction);

        verifyProcessingStoppedOnceExceptionIsThrown(composedUnderTest, throwingFunction);
    }

    private void verifyProcessingStoppedOnceExceptionIsThrown(final ThrowingFunction<String, String, Exception> composedUnderTest, final TestThrowingFunction throwingFunction) throws Exception {
        try {
            composedUnderTest.apply("test-throwing-exception-in-function");
            fail("Expected a test exception to be thrown!");
        } catch (final Exception exception) {
            assertThat(exception).hasMessage("test-thrown-exception");
        } finally {
            final InOrder inOrder = inOrder(underTest, throwingFunction);
            inOrder.verify(underTest).apply("test-throwing-exception-in-function");
            inOrder.verifyNoMoreInteractions();
        }
    }

    private class TestThrowingFunction implements ThrowingFunction<String, String, Exception> {
        @Override
        public String apply(final String s) throws Exception {
            return s;
        }
    }

    private class TestFunction implements Function<String, String> {
        @Override
        public String apply(final String s) {
            return s;
        }
    }
}
