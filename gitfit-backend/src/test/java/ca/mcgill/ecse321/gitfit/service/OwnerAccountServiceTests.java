package ca.mcgill.ecse321.gitfit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.gitfit.dao.CustomerRepository;
import ca.mcgill.ecse321.gitfit.dao.InstructorRepository;
import ca.mcgill.ecse321.gitfit.dao.OwnerRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.Owner;
import ca.mcgill.ecse321.gitfit.model.SportCenter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest
public class OwnerAccountServiceTests {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private ValidatorService validatorService;

    @Mock
    private SportCenterService sportCenterService;

    @InjectMocks
    private OwnerAccountService ownerAccountService;

    private static final String CENTER_NAME = "McGill Gym";
    private static final int CENTER_MAX_CAPACITY = 100;
    private static final Time CENTER_OPEN_TIME = Time.valueOf("08:00:00");
    private static final Time CENTER_CLOSE_TIME = Time.valueOf("22:00:00");
    private static final String username = "AnakinTheBest";
    private static final String email = "anakin@starwars.com";
    private static final String password = "theForce123";
    private static final String lastName = "Skywalker";
    private static final String firstName = "Anakin";

    private static final SportCenter sportCenter = new SportCenter(CENTER_NAME, CENTER_MAX_CAPACITY,
            CENTER_OPEN_TIME, CENTER_CLOSE_TIME, username, email, password, lastName, firstName);

    @BeforeEach
    public void setMockOutput() {
        lenient().when(sportCenterService.getSportCenter()).thenReturn(sportCenter);

        lenient().when(ownerRepository.save(any(Owner.class))).thenAnswer((InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        });

        lenient().when(ownerRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Owner> owners = new ArrayList<>();
            owners.add(sportCenter.getOwner());
            return owners;
        });

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        doAnswer((Answer<Void>) invocation -> {

            Object dtoToValidate = invocation.getArgument(0);
            Set<ConstraintViolation<Object>> violations = validator.validate(dtoToValidate);
            if (!violations.isEmpty()) {
                StringBuilder combinedViolations = new StringBuilder();
                for (ConstraintViolation<Object> violation : violations) {
                    combinedViolations.append(violation.getMessage()).append("; ");
                }
                throw new SportCenterException(HttpStatus.BAD_REQUEST, combinedViolations.toString().trim());
            }
            return null;
        }).when(validatorService).validate(any());

    }

    @Test
    public void testGetOwner() {
        // Create owner params
        String username = "AnakinTheBest";
        String email = "anakin@starwars.com";
        String password = "theForce123";
        String lastName = "Skywalker";
        String firstName = "Anakin";

        Owner owner = null;
        try {
            owner = ownerAccountService.getOwner();
        } catch (SportCenterException e) {
            assertNull(owner);
            assertEquals("No current owner.", e.getMessage());
        }
        assertNotNull(owner);
        assertEquals(username, owner.getUsername());
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());
        assertEquals(lastName, owner.getLastName());
        assertEquals(firstName, owner.getFirstName());
    }

    @Test
    public void testUpdateOwnerPassword() {
        String newPassword = "theForce456";

        when(ownerRepository.save(any(Owner.class))).thenReturn(sportCenter.getOwner());

        Owner owner = ownerAccountService.updateOwnerPassword(newPassword);

        assertNotNull(owner);
        assertEquals(newPassword, owner.getPassword());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    public void testUpdateOwnerPasswordInvalidPassword() {
        String newPassword = "theForceNoDigits";

        when(ownerRepository.save(any(Owner.class))).thenReturn(sportCenter.getOwner());

        try {
            ownerAccountService.updateOwnerPassword(newPassword);
        } catch (SportCenterException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
            assertEquals("Password must contain at least one digit, one lowercase letter, and one uppercase letter;",
                    e.getMessage());
        }
    }
}
