package rental.presentation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rental.presentation.dto.response.common.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException notFoundException) {
        log.warn(notFoundException.getMessage());
        return ErrorResponse.of(HttpStatus.NOT_FOUND.value(),
                notFoundException.getCode(),
                notFoundException.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerException(InternalServerException internalServerException) {
        log.warn(internalServerException.getMessage());
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                internalServerException.getCode(),
                internalServerException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(MethodArgumentNotValidException ex) {
        log.warn(ex.getMessage());
        return ErrorResponse.of(HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getMessage());
    }
}
