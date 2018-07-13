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
import org.junit.BeforeClass;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import static org.assertj.core.api.Assertions.assertThat;

public class SymmetricGeneratorTest {

    private final SymmetricGenerator underTest = new SymmetricGenerator();

    @BeforeClass
    public static void beforeClass() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void generateKeysShouldHandleAES() {

        final SecretKey result = underTest.generateKey(SymmetricCipher.AES_CBC_PKCS7, 256);

        assertThat(result.getEncoded()).hasSize(256 / 8);
        assertThat(result.getAlgorithm()).isEqualTo("AES");
    }

    @Test
    public void shouldSuccessfullyGenerateParametersForAesGcmPkcs7() {

        final AlgorithmParameterSpec result = underTest.generateParameters(SymmetricCipher.AES_CBC_PKCS7);

        assertThat(result).isInstanceOf(IvParameterSpec.class);
        assertThat(((IvParameterSpec) result).getIV()).hasSize(16); // TODO - Review extraction mechanism
    }

    @Test
    public void shouldSuccessfullyGenerateParametersForAesGcmNone() {

        final AlgorithmParameterSpec result = underTest.generateParameters(SymmetricCipher.AES_GCM_NONE);

        assertThat(result).isInstanceOf(GCMParameterSpec.class);

        assertThat(result).hasFieldOrProperty("iv");
        assertThat(((GCMParameterSpec) result).getIV()).hasSize(12); // TODO - Review extraction mechanism

        assertThat(result).hasFieldOrProperty("tLen");
        assertThat(((GCMParameterSpec) result).getTLen()).isEqualTo(128); // TODO - Review extraction mechanism
    }

}
