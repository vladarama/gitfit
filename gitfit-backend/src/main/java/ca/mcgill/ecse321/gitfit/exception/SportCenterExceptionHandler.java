package ca.mcgill.ecse321.gitfit.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ca.mcgill.ecse321.gitfit.dto.ErrorDto;

@ControllerAdvice
public class SportCenterExceptionHandler {
    @ExceptionHandler(SportCenterException.class)
    public ResponseEntity<ErrorDto> handleSportCenterException(SportCenterException e) {
        return new ResponseEntity<ErrorDto>(new ErrorDto(e.getMessage()), e.getStatus());
    }
}