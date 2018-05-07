/*
 * Copyright 2018 James Bacon
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

package uk.co.baconi.secure.api.common;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UriBuilderTest {

    @Test
    public void shouldBeAbleToBuildUris() {
        final URI uri = UriBuilder.builder().append("/bob").build();

        assertThat(uri.toString())
                .isEqualTo("/bob");
    }

    @Test
    public void shouldBeAbleToEncodePartsOfTheUri() {
        final URI uri = UriBuilder.builder().append("/bob/").appendEncoded("MjdkODM2ZDAtMmUwNy00NDliLWIxZDYtMjk0MjQzNTEzNzZh==").build();

        assertThat(uri.toString())
                .isEqualTo("/bob/MjdkODM2ZDAtMmUwNy00NDliLWIxZDYtMjk0MjQzNTEzNzZh%3D%3D");
    }

    @Test
    public void shouldBeAbleToEncodeAllOfTheUri() {
        final URI uri = UriBuilder.builder().appendEncoded("/bob/MjdkODM2ZDAtMmUwNy00NDliLWIxZDYtMjk0MjQzNTEzNzZh==").build();

        assertThat(uri.toString())
                .isEqualTo("%2fbob%2fMjdkODM2ZDAtMmUwNy00NDliLWIxZDYtMjk0MjQzNTEzNzZh%3d%3d");
    }

    @Test
    public void shouldOnlyThrowEncodingExceptionsOnBuild() {

        final UriBuilder builder = UriBuilder.builder().withCharset("F").append("/bob/").appendEncoded("&");

        assertThatThrownBy(builder::build)
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(UnsupportedEncodingException.class);
    }

}
