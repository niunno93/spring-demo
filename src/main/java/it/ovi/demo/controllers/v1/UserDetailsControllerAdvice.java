package it.ovi.demo.controllers.v1;

import it.ovi.demo.controllers.dto.ErrorResponse;
import it.ovi.demo.errors.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice(basePackageClasses = UserDetailsController.class)
public class UserDetailsControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    ResponseEntity<ErrorResponse> handleNoSuchElementException(HttpServletRequest request, NoSuchElementException ex) {
        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(body, null, body.getStatus());
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseBody
    ResponseEntity<ErrorResponse> handleInvalidRequestException(HttpServletRequest request, InvalidRequestException ex) {
        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(body, null, body.getStatus());
    }
}
