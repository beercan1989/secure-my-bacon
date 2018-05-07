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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UriBuilder {

    private final List<Supplier<String>> pathParts = new ArrayList<>();
    private String charset = StandardCharsets.UTF_8.name();

    public static UriBuilder builder() {
        return new UriBuilder();
    }

    public UriBuilder withCharset(final String charset) {
        this.charset = charset;
        return this;
    }

    public UriBuilder append(final String string) {
        pathParts.add(() -> string);
        return this;
    }

    public UriBuilder appendEncoded(final UUID uuid) {
        pathParts.add(() -> urlEncode(uuid.toString()));
        return this;
    }

    public UriBuilder appendEncoded(final String string) {
        pathParts.add(() -> urlEncode(string));
        return this;
    }

    public URI build() {
        final String url = pathParts
                .stream()
                .map(Supplier::get)
                .collect(Collectors.joining());

        return URI.create(url);
    }

    private String urlEncode(final String string) {
        try {
            return URLEncoder.encode(string, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
