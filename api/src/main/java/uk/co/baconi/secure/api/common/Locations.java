package uk.co.baconi.secure.api.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
// TODO - Consider renaming and rebuilding as a URL builder?
public class Locations {
    
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final String PASSWORDS = "/passwords";
    public static final String BY_UUID = "/by-uuid/";
    public static final String FOR_USER = "/for-user/";

    public static URI passwordByUuid(final UUID uuid) {
        try {
            return passwordByUuid(uuid, DEFAULT_CHARSET);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static URI passwordByUuid(final UUID uuid, final Charset charset) throws UnsupportedEncodingException {
        final String encodedUuid = URLEncoder.encode(uuid.toString(), charset.name());
        return URI.create(PASSWORDS + BY_UUID + encodedUuid);
    }
}
