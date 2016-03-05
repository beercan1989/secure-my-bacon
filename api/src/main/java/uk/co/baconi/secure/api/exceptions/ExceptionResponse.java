package uk.co.baconi.secure.api.exceptions;

import java.util.UUID;

public class ExceptionResponse implements BaseErrorResponse {

    private final UUID uuid;
    private final String name;

    public ExceptionResponse(final Exception exception) {
        this.uuid = UUID.randomUUID();
        this.name = exception.getClass().getName();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }
}
