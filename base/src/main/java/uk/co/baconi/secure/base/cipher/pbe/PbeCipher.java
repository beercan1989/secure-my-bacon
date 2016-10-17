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

package uk.co.baconi.secure.base.cipher.pbe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * All supported passed based encryption cipher types for securing users with their password.
 */
@Getter
@ToString
@AllArgsConstructor
public enum PbeCipher {


    /**
     * Scheme: PKCS5 Scheme 2
     * Key Length: variable
     * Char to Byte conversion: UTF-8 chars
     */
    PBKDF2_HMAC_SHA1_AND_UTF8("PBKDF2WithHmacSHA1AndUTF8"),

    /**
     * Scheme: PKCS12
     * Key Length: 256
     * Char to Byte conversion: 16 bit chars
     */
    PBE_SHA256_AES256_BC("PBEWithSHA256And256BitAES-CBC-BC");

    private final String type;
}
