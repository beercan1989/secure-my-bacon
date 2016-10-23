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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import uk.co.baconi.secure.base.BaseUnitTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class CharsetCodecTest extends BaseUnitTest {

    private final String string;
    private final byte[] bytes;
    private final CharsetCodec underTest = new CharsetCodec();

    public CharsetCodecTest(final String string, final byte[] bytes) {
        this.string = string;
        this.bytes = bytes;
    }

    @Test
    public void shouldBeAbleToEncodeString() {

        final byte[] encodedResult = underTest.encode(string);
        assertThat(encodedResult).containsExactly(bytes);
    }

    @Test
    public void shouldBeAbleToDecodeString() {

        final String decodedResult = underTest.decode(bytes);
        assertThat(decodedResult).containsSequence(string);
        assertThat(decodedResult).isEqualToIgnoringWhitespace(string);
        assertThat(decodedResult).isEqualTo(string);
    }

    @Parameters(name = "{index}: [{0}]")
    public static Iterable<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {"", new byte[]{}},
                        {" ", new byte[]{32}},
                        {"  ", new byte[]{32, 32}},
                        {"test", new byte[]{116, 101, 115, 116}},
                        {"test-", new byte[]{116, 101, 115, 116, 45}},
                        {"test-s", new byte[]{116, 101, 115, 116, 45, 115}},
                        {"test-st", new byte[]{116, 101, 115, 116, 45, 115, 116}},
                        {"test-str", new byte[]{116, 101, 115, 116, 45, 115, 116, 114}},
                        {"test-stri", new byte[]{116, 101, 115, 116, 45, 115, 116, 114, 105}},
                        {"test-strin", new byte[]{116, 101, 115, 116, 45, 115, 116, 114, 105, 110}},
                        {"test-string", new byte[]{116, 101, 115, 116, 45, 115, 116, 114, 105, 110, 103}},
                }
        );
    }
}
