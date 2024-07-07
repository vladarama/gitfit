
package ca.mcgill.ecse321.gitfit.service;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * This class is responsible for validating objects using the Java Bean
 * Validation API
 * 
 * @author Jatin Patel (Jatin-Pat)
 */
@Service
public class ValidatorService {

    private Validator validator;

    /**
     * Constructs a new ValidatorService instance.
     * Initializes the validator using the default validator factory
     * 
     * @author Jatin Patel (Jatin-Pat)
     */
    public ValidatorService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Validates the given object using the Java Bean Validation API.
     * If any constraint violations are found, a SportCenterException is thrown with
     * a message containing the first violation
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param dto
     * @throws SportCenterException if any constraint violations are found.
     */
    public <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            StringBuilder combinedViolations = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                combinedViolations.append(violation.getMessage()).append("; ");
            }
            throw new SportCenterException(HttpStatus.BAD_REQUEST, combinedViolations.toString());
        }
    }
}
