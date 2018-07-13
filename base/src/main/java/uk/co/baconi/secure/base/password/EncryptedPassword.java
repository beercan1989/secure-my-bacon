/*
 * Copyright 2015 James Bacon
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

package uk.co.baconi.secure.base.password;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.baconi.secure.base.cipher.Encrypted;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptedPassword extends Password<EncryptedPassword> implements Encrypted {

    protected EncryptedPassword(final String whereFor, final String username, final byte[] password) {
        super(whereFor, username, password);
    }

    protected EncryptedPassword(final UUID uuid, final String whereFor, final String username, final byte[] password) {
        super(uuid, whereFor, username, password);
    }

}
