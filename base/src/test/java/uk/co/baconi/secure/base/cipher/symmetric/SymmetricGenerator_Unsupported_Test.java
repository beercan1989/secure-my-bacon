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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import uk.co.baconi.secure.base.cipher.UnsupportedCipherTypeException;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SymmetricCipher.class)
public class SymmetricGenerator_Unsupported_Test {

    private static final SymmetricCipher[] ORIGINAL_CIPHER_ENUMS = SymmetricCipher.values();
    private static final SymmetricCipher UNSUPPORTED_CIPHER = PowerMockito.mock(SymmetricCipher.class);

    private final SecureRandom secureRandom = mock(SecureRandom.class);
    private final KeyGeneratorFactory keyGeneratorFactory = mock(KeyGeneratorFactory.class);
    private final SymmetricGenerator underTest = new SymmetricGenerator(secureRandom, keyGeneratorFactory);

    @BeforeClass
    public static void beforeClass() {
        Whitebox.setInternalState(UNSUPPORTED_CIPHER, "name", "UnsupportedTestCipher");
        Whitebox.setInternalState(UNSUPPORTED_CIPHER, "ordinal", ORIGINAL_CIPHER_ENUMS.length);

        final List<SymmetricCipher> newEnumList = new ArrayList<>(Arrays.asList(ORIGINAL_CIPHER_ENUMS));
        newEnumList.add(UNSUPPORTED_CIPHER);
        final SymmetricCipher[] newEnumValues = newEnumList.toArray(new SymmetricCipher[newEnumList.size()]);

        PowerMockito.mockStatic(SymmetricCipher.class);
        PowerMockito.when(SymmetricCipher.values()).thenReturn(newEnumValues);
    }

    @Before
    public void before() {
        reset(secureRandom, keyGeneratorFactory);
    }

    @Test
    public void generateKeysShouldThrowAnErrorOnUnsupportedCipher() {
        try {
            final SecretKey result = underTest.generateKey(UNSUPPORTED_CIPHER, 8);
            fail("An UnsupportedCipherTypeException should have been thrown but instead we got a result of: %s", result);
        } catch (final UnsupportedCipherTypeException exception) {
            assertThat(exception).hasMessageContaining("generate-key");
            assertThat(exception).hasNoCause();
        }
    }

    @Test
    public void generateKeysShouldThrowAnErrorOnNoSuchAlgorithm() throws NoSuchAlgorithmException {
        when(keyGeneratorFactory.apply(anyString())).thenThrow(new NoSuchAlgorithmException("test-no-such-algorithm"));
        try {
            final SecretKey result = underTest.generateKey(SymmetricCipher.AES_CBC_PKCS7, 8);
            fail("An UnsupportedCipherTypeException should have been thrown but instead we got a result of: %s", result);
        } catch (final UnsupportedCipherTypeException exception) {
            assertThat(exception).hasMessageContaining("generate-secret-key");
            assertThat(exception).hasCauseInstanceOf(NoSuchAlgorithmException.class);
        }
    }

    @Test
    public void generateParametersShouldThrowAnErrorOnUnsupportedCipher() {
        try {
            final AlgorithmParameterSpec result = underTest.generateParameters(UNSUPPORTED_CIPHER);
            fail("An UnsupportedCipherTypeException should have been thrown but instead we got a result of: %s", result);
        } catch (final UnsupportedCipherTypeException exception) {
            assertThat(exception).hasMessageContaining("generate-parameters");
            assertThat(exception).hasNoCause();
        }
    }
}
