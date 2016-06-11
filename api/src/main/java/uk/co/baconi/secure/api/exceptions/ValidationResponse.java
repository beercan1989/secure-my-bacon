package uk.co.baconi.secure.api.exceptions;

import lombok.Getter;
import lombok.ToString;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@ToString
public class ValidationResponse implements BaseErrorResponse {

    private final UUID uuid;
    private final List<String> errors;

    private ValidationResponse(final List<String> errors) {
        this.uuid = UUID.randomUUID();
        this.errors = errors;
    }

    public ValidationResponse(final ConstraintViolationException exception) {
        this(
                exception.
                        getConstraintViolations().
                        stream().
                        map(
                                ConstraintViolation::getMessage
                        ).
                        collect(
                                Collectors.toList()
                        )
        );
    }

    public ValidationResponse(final MethodArgumentTypeMismatchException exception) {
        this(
                Collections.singletonList(
                        String.format("Param '%s' requires type '%s' but was provided '%s'",
                                exception.getName(),
                                exception.getRequiredType().getName(),
                                exception.getValue()
                        )
                )
        );
    }
}
