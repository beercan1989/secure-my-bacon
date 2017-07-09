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

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ThrowingConsumerTest {

    private final TestThrowingConsumer underTest = mock(TestThrowingConsumer.class, CALLS_REAL_METHODS);

    @Before
    public void before() throws Exception {
        reset(underTest);
    }

    @Test
    public void shouldCallAcceptOnWith() throws Exception {

        underTest.with("test-with");

        verify(underTest).with("test-with");
        verify(underTest).accept("test-with");
        verifyNoMoreInteractions(underTest);
    }

    @Test
    public void shouldSupportCallingAnotherConsumer() throws Exception {
        final TestConsumer consumer = mock(TestConsumer.class, CALLS_REAL_METHODS);

        underTest.andThen(consumer).accept("test-calling-consumer");

        final InOrder inOrder = inOrder(underTest, consumer);
        inOrder.verify(underTest).accept("test-calling-consumer");
        inOrder.verify(consumer).accept("test-calling-consumer");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldSupportCallingAnotherThrowingConsumer() throws Exception {
        final TestThrowingConsumer throwingConsumer = mock(TestThrowingConsumer.class, CALLS_REAL_METHODS);

        underTest.andThen(throwingConsumer).accept("test-calling-throwing-consumer");

        final InOrder inOrder = inOrder(underTest, throwingConsumer);
        inOrder.verify(underTest).accept("test-calling-throwing-consumer");
        inOrder.verify(throwingConsumer).accept("test-calling-throwing-consumer");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void shouldStopProcessingChainIfExceptionThrown() throws Exception {
        doThrow(new Exception("test-thrown-exception")).when(underTest).accept(anyString());

        final TestThrowingConsumer throwingConsumer = mock(TestThrowingConsumer.class, CALLS_REAL_METHODS);

        final ThrowingConsumer<String, Exception> chainedUnderTest = underTest.andThen(throwingConsumer);

        try {
            chainedUnderTest.accept("test-throwing-exception-in-consumer");
            fail("Expected a test exception to be thrown!");
        } catch (final Exception exception) {
            assertThat(exception).hasMessage("test-thrown-exception");
        } finally {
            final InOrder inOrder = inOrder(underTest, throwingConsumer);
            inOrder.verify(underTest).accept("test-throwing-exception-in-consumer");
            inOrder.verifyNoMoreInteractions();
        }
    }

    private abstract class TestThrowingConsumer implements ThrowingConsumer<String, Exception> {}
    private abstract class TestConsumer implements Consumer<String> {}
}
