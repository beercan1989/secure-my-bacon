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

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import uk.co.baconi.secure.base.cipher.UnsupportedCipherTypeException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SymmetricCipher.class)
public class SymmetricGenerator_Unsupported_Test {

    private static final SymmetricCipher[] ORIGINAL_CIPHER_ENUMS = SymmetricCipher.values();
    private static final SymmetricCipher UNSUPPORTED_CIPHER = PowerMockito.mock(SymmetricCipher.class);

    private final SecureRandom secureRandom = mock(SecureRandom.class);
    private final SymmetricGenerator underTest = new SymmetricGenerator(secureRandom);

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

    @Test
    public void generateKeysShouldThrowAnErrorOnUnsupportedCipher() {
        try {
            underTest.generateKey(UNSUPPORTED_CIPHER, 8);
            assertNeverRun();
        } catch (final UnsupportedCipherTypeException exception) {
            assertThat(exception).hasMessageContaining("generate-key");
        }
    }

    @Test
    public void generateParametersShouldThrowAnErrorOnUnsupportedCipher() {
        try {
            underTest.generateParameters(UNSUPPORTED_CIPHER);
            assertNeverRun();
        } catch (final UnsupportedCipherTypeException exception) {
            assertThat(exception).hasMessageContaining("generate-parameters");
        }
    }

    private void assertNeverRun() {
        throw new AssertionError("Should never be run!");
    }
}
