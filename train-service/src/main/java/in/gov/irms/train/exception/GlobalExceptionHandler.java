package in.gov.irms.train.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTrainNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleInvalidTrainNumberException(InvalidTrainNumberException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(HttpStatus.BAD_REQUEST, request, exception));
    }

    @ExceptionHandler(StationServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleStationServiceException(StationServiceException exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, exception));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleGenericException(Exception exception, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, exception));
    }

}
