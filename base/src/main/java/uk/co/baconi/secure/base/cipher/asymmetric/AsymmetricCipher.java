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

package uk.co.baconi.secure.base.cipher.asymmetric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * All supported asymmetric cipher types for securing bags for users.
 * <p>
 * Layout: {CIPHER}_{MODE}_{PADDING}
 */
@Getter
@ToString
@AllArgsConstructor
public enum AsymmetricCipher {

    RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding");

    private final String type;
}
