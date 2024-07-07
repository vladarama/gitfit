package ca.mcgill.ecse321.gitfit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public class SportCenterException extends RuntimeException {
    @NonNull
    private HttpStatus status;

    public SportCenterException(@NonNull HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    @NonNull
    public HttpStatus getStatus() {
        return status;
    }
}