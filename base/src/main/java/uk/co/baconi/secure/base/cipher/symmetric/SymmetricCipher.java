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

package uk.co.baconi.secure.base.cipher.symmetric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.crypto.KeyGenerator;
import java.util.function.Supplier;

/**
 * All supported symmetric cipher types for securing passwords with bags.
 *
 * Layout: {CIPHER}_{MODE}_{PADDING}
 *
 */
@Getter
@ToString
@AllArgsConstructor
public enum SymmetricCipher {

    AES_CBC_PKCS7("AES/CBC/PKCS7Padding", "AES");

    private final String type;
    private final String keyGeneratorType;
}