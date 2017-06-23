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

package uk.co.baconi.secure.base.cipher.symmetric;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.SpringApplication;
import uk.co.baconi.function.ConsumerWithException;
import uk.co.baconi.secure.base.cipher.UnsupportedCipherTypeException;
import uk.co.baconi.secure.base.cipher.charset.CharsetCodec;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
public class SymmetricEngineTest {

    private final SymmetricEngine underTest = new SymmetricEngine(new CharsetCodec());

    @Test
    public void doFinalShouldHandleBadCipherExceptions() throws Exception {

        final Function<Exception, SymmetricEngineTestException> onError = mock(TestExceptionHandlerFunction.class);
        final byte[] cipherText = "test-data".getBytes();

        final ConsumerWithException<Exception> verifyDoFinal = (exception) -> {

            final Cipher engine = PowerMockito.mock(Cipher.class);
            when(engine.doFinal(any())).thenThrow(exception);

            when(onError.apply(any())).then(invocation -> new SymmetricEngineTestException(invocation.getArgumentAt(0, Exception.class)));

            try {
                underTest.doFinal(engine, cipherText, onError);
                throw new AssertionError("Expected an exception to be thrown!");
            } catch (final SymmetricEngineTestException e) {
                assertThat(e).hasCause(exception);
            } finally {
                verify(engine).doFinal(cipherText);
                verify(onError).apply(exception);
            }
        };


        verifyDoFinal.with(new BadPaddingException("t"));
        verifyDoFinal.with(new IllegalBlockSizeException("b"));
    }

    @Test
    public void initCipherShouldHandleNoSuchCipherExceptions() throws Exception {

        PowerMockito.mockStatic(Cipher.class);
        when(Cipher.getInstance(anyString())).thenThrow(NoSuchPaddingException.class, NoSuchAlgorithmException.class);

        try {
            underTest.initCipher(0, SymmetricCipher.AES_CBC_PKCS7, mock(SecretKey.class), mock(AlgorithmParameterSpec.class));
            throw new AssertionError("Expected an exception to be thrown!");
        } catch (final UnsupportedCipherTypeException e) {
            assertThat(e).hasCauseInstanceOf(NoSuchPaddingException.class);
        }

        try {
            underTest.initCipher(0, SymmetricCipher.AES_CBC_PKCS7, mock(SecretKey.class), mock(AlgorithmParameterSpec.class));
            throw new AssertionError("Expected an exception to be thrown!");
        } catch (final UnsupportedCipherTypeException e) {
            assertThat(e).hasCauseInstanceOf(NoSuchAlgorithmException.class);
        }
    }

    @Test
    public void initCipherShouldHandleInvalidCipherExceptions() throws Exception {

        PowerMockito.mockStatic(Cipher.class);

        final Cipher cipher = PowerMockito.mock(Cipher.class);
        when(Cipher.getInstance(anyString())).thenReturn(cipher);

        doThrow(InvalidKeyException.class).when(cipher).init(anyInt(), any(SecretKey.class), any(AlgorithmParameterSpec.class));

        try {
            underTest.initCipher(0, SymmetricCipher.AES_CBC_PKCS7, mock(SecretKey.class), mock(AlgorithmParameterSpec.class));
            throw new AssertionError("Expected an exception to be thrown!");
        } catch (final UnsupportedCipherTypeException e) {
            assertThat(e).hasCauseInstanceOf(InvalidKeyException.class);
        }

        doThrow(InvalidAlgorithmParameterException.class).when(cipher).init(anyInt(), any(SecretKey.class), any(AlgorithmParameterSpec.class));

        try {
            underTest.initCipher(0, SymmetricCipher.AES_CBC_PKCS7, mock(SecretKey.class), mock(AlgorithmParameterSpec.class));
            throw new AssertionError("Expected an exception to be thrown!");
        } catch (final UnsupportedCipherTypeException e) {
            assertThat(e).hasCauseInstanceOf(InvalidAlgorithmParameterException.class);
        }
    }

    private class SymmetricEngineTestException extends Exception {
        public SymmetricEngineTestException(Throwable cause) {
            super(cause);
        }
    }
    private interface TestExceptionHandlerFunction extends Function<Exception, SymmetricEngineTestException> {}

}
