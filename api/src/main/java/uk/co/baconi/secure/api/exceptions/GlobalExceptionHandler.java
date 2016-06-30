package uk.co.baconi.secure.api.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @Order(Ordered.LOWEST_PRECEDENCE)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleExceptions(final HttpServletRequest request,
                                                              final Exception exception) {

        return handleResponse(new ExceptionResponse(exception), request, HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationResponse> handleResourceNotFoundException(final HttpServletRequest request,
                                                                              final ConstraintViolationException exception) {

        return handleValidationResponse(new ValidationResponse(exception), request, exception);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationResponse> handleMethodArgumentTypeMismatchException(final HttpServletRequest request,
                                                                                        final MethodArgumentTypeMismatchException exception) {

        return handleValidationResponse(new ValidationResponse(exception), request, exception);
    }

    private ResponseEntity<ValidationResponse> handleValidationResponse(final ValidationResponse response,
                                                                        final HttpServletRequest request,
                                                                        final Exception exception) {

        return handleResponse(response, request, HttpStatus.BAD_REQUEST, exception);
    }

    private <A extends BaseErrorResponse> ResponseEntity<A> handleResponse(final A response,
                                                                           final HttpServletRequest request,
                                                                           final HttpStatus httpStatus,
                                                                           final Exception exception) {
        log.error(
                "Error [{}] on url [{}] of [{}] with message [{}]",
                response.getUuid(),
                request.getRequestURL(),
                exception.getClass().getName(),
                exception.getMessage()
        );

        log.debug("Stacktrace for [{}]:", response.getUuid(), exception);

        log.trace("Response for [{}]:", response.getUuid(), response);

        return ResponseEntity.status(httpStatus).body(response);
    }

}
