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

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.co.baconi.function.ThrowingConsumer;
import uk.co.baconi.secure.base.cipher.DecryptionException;
import uk.co.baconi.secure.base.cipher.EncryptionException;
import uk.co.baconi.secure.base.cipher.UnsupportedCipherTypeException;
import uk.co.baconi.secure.base.cipher.charset.CharsetCodec;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cipher.class)
public class SymmetricEngineTest {

    //
    // GCM test data
    //
    private static final byte[] GCM_CIPHER_TEXT = Base64.decode("SUTBvAf8mB1iqoDj2TpmRddtyR4GjhlyaZIdMvwbvi2+H1NQeeE2BxuGUFJaFoUtLmm059UYIA==");
    private static final byte[] GCM_AUTH_TAG = Base64.decode("KXrGQR7udgrAgfxctvJAkg==");
    private static final byte[] GCM_IV = Base64.decode("1MmxNgwpU42Mi77Z");
    private static final byte[] GCM_KEY = Base64.decode("TIZTMfJvxTSAlyjMP76QWo9Src/+ALpcaI59Kx8M2e4=");

    private final SymmetricEngine underTest = new SymmetricEngine(new CharsetCodec());

    @BeforeClass
    public static void beforeClass() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void decryptWithGcmShouldWorkAsExpected() throws IOException, DecryptionException {

        final SymmetricEngine engine = new SymmetricEngine(new CharsetCodec());
        final GCMParameterSpec parameterSpec = new GCMParameterSpec(128, GCM_IV);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(GCM_CIPHER_TEXT);
        outputStream.write(GCM_AUTH_TAG);
        final SecretKey secretKey = new SecretKeySpec(GCM_KEY, "AES");
        final String result = engine.decrypt(SymmetricCipher.AES_GCM_NONE, secretKey, parameterSpec, outputStream.toByteArray());

        assertThat(result).isEqualTo("{\"username\":\"Joe Bloggs\",\"authToken\":\"fake-auth-token\"}");
    }

    @Test
    public void encryptWithGcmShouldWorkAsExpected() throws IOException, EncryptionException {

        final SymmetricEngine engine = new SymmetricEngine(new CharsetCodec());
        final GCMParameterSpec parameterSpec = new GCMParameterSpec(128, GCM_IV);
        final SecretKey secretKey = new SecretKeySpec(GCM_KEY, "AES");
        final String plainText = "{\"username\":\"Joe Bloggs\",\"authToken\":\"fake-auth-token\"}";
        final byte[] result = engine.encrypt(SymmetricCipher.AES_GCM_NONE, secretKey, parameterSpec, plainText);

        assertThat(result).startsWith(GCM_CIPHER_TEXT);
        assertThat(result).endsWith(GCM_AUTH_TAG);
    }

    @Test
    public void doFinalShouldHandleBadCipherExceptions() throws Exception {

        final TestExceptionHandlerFunction onError = mock(TestExceptionHandlerFunction.class);
        final byte[] cipherText = "test-data".getBytes();

        final ThrowingConsumer<GeneralSecurityException, Exception> verifyDoFinal = (exception) -> {

            final Cipher engine = PowerMockito.mock(Cipher.class);
            when(engine.doFinal(any())).thenThrow(exception);

            when(onError.apply(any())).then(invocation -> new SymmetricEngineTestException(invocation.getArgumentAt(0, Exception.class)));

            try {
                underTest.doFinal(engine, cipherText, onError);
                fail("Expected an exception to be thrown while ciphering!");
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
        when(Cipher.getInstance(anyString())).thenThrow(new NoSuchPaddingException(), new NoSuchAlgorithmException());

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
        SymmetricEngineTestException(Throwable cause) {
            super(cause);
        }
    }
    private interface TestExceptionHandlerFunction extends Function<GeneralSecurityException, SymmetricEngineTestException> {}

}
