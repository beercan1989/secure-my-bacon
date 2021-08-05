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

package uk.co.baconi.secure.base.cipher;

import org.junit.Test;
import uk.co.baconi.secure.base.BaseUnitTest;
import uk.co.baconi.secure.base.cipher.symmetric.SymmetricCipher;

import static org.assertj.core.api.Assertions.assertThat;

public class UnsupportedCipherTypeExceptionTest extends BaseUnitTest {

    @Test
    public void shouldProvideUsefulMessage() {

        final UnsupportedCipherTypeException underTest = new UnsupportedCipherTypeException(
                SymmetricCipher.AES_CBC_PKCS7,
                "test-operation"
        );

        assertThat(underTest).hasMessage("Unsupported cipher [SymmetricCipher.AES_CBC_PKCS7(type=AES/CBC/PKCS7Padding, keyGeneratorType=AES)] for operation [test-operation]");
        assertThat(underTest).hasNoCause();
    }

    @Test
    public void shouldProvideUsefulMessageWithCause() {

        final UnsupportedCipherTypeException underTest = new UnsupportedCipherTypeException(
                SymmetricCipher.AES_CBC_PKCS7,
                "test-operation-with-exception",
                new Exception("test-exception")
        );

        assertThat(underTest).hasMessage("Unsupported cipher [SymmetricCipher.AES_CBC_PKCS7(type=AES/CBC/PKCS7Padding, keyGeneratorType=AES)] for operation [test-operation-with-exception]");
        assertThat(underTest).hasCauseInstanceOf(Exception.class);
    }
}
