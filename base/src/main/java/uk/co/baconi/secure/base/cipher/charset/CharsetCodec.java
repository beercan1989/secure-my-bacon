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

package uk.co.baconi.secure.base.cipher.charset;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Provides a mechanism to encode and decode a String into a byte array based on charset
 */
@Service
public class CharsetCodec {

    private final Charset charset = StandardCharsets.UTF_8;

    public byte[] encode(final String string) {

        final CharBuffer charBuffer = CharBuffer.wrap(string);
        final ByteBuffer encoded = charset.encode(charBuffer);

        return encoded.array();
    }

    public String decode(final byte[] bytes) {

        final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        final CharBuffer decoded = charset.decode(byteBuffer);

        return decoded.toString();
    }
}
