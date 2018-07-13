package uk.co.baconi.secure.api.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Locations {

    public static final String PASSWORDS = "/passwords";
    public static final String USERS = "/users";
    public static final String BAGS = "/bags";

    public static final String BY_UUID = "/by-uuid/";
    public static final String BY_NAME = "/by-name/";
    public static final String FOR_USER = "/for-user/";

    public static URI passwordByUuid(final UUID uuid) {
        return UriBuilder
                .builder()
                .append(PASSWORDS)
                .append(BY_UUID)
                .appendEncoded(uuid)
                .build();
    }

    public static URI userByName(final String name) {
        return UriBuilder
                .builder()
                .append(USERS)
                .append(BY_NAME)
                .appendEncoded(name)
                .build();
    }

    public static URI bagByName(final String name) {
        return UriBuilder
                .builder()
                .append(BAGS)
                .append(BY_NAME)
                .appendEncoded(name)
                .build();
    }
}
