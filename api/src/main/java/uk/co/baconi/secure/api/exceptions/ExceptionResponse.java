package uk.co.baconi.secure.api.exceptions;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class ExceptionResponse implements BaseErrorResponse {

    private final UUID uuid;
    private final String name;

    public ExceptionResponse(final Exception exception) {
        this.uuid = UUID.randomUUID();
        this.name = exception.getClass().getName();
    }

}
