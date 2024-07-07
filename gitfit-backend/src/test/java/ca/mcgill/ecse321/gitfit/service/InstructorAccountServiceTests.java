package ca.mcgill.ecse321.gitfit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.SportCenter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest
public class InstructorAccountServiceTests {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SportCenterService sportCenterService;

    @Mock
    private ValidatorService validatorService;

    @InjectMocks
    private InstructorAccountService instructorAccountService;

    private static final String validInstructor = "TestInstructor";
    private static final String nonExistingInstructor = "InstructorNotFound";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(sportCenterService.getSportCenter()).thenReturn(new SportCenter());
        lenient().when(instructorRepository.save(any(Instructor.class))).thenAnswer((InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        });

        lenient().when(instructorRepository.findInstructorByUsername(any(String.class)))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(validInstructor)) {
                        Instructor instructor = new Instructor();
                        instructor.setUsername(validInstructor);
                        return instructor;
                    } else {
                        return null;
                    }
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
    public void testGetExistingInstructor() {
        Instructor instructor = instructorAccountService.getInstructor(validInstructor);
        assertNotNull(instructor);
        assertEquals(validInstructor, instructor.getUsername());
    }

    @Test
    public void testGetNonExistingInstructor() {
        try {
            instructorAccountService.getInstructor(nonExistingInstructor);
        } catch (SportCenterException e) {
            assertEquals("Instructor not found.", e.getMessage());
        }
    }

    @Test
    public void testGetAllInstructors() {
        Instructor instructor1 = new Instructor();
        Instructor instructor2 = new Instructor();

        List<Instructor> mockInstructor = Arrays.asList(instructor1, instructor2);
        lenient().when(instructorRepository.findAll()).thenReturn(mockInstructor);

        List<Instructor> retrievedCustomers = instructorAccountService.getAllInstructors();

        assertNotNull(retrievedCustomers);
        assertEquals(mockInstructor.size(), retrievedCustomers.size());
        assertTrue(retrievedCustomers.contains(instructor1));
        assertTrue(retrievedCustomers.contains(instructor2));
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    public void testCreateValidInstructor() {
        // Create instructor object params
        String username = "Obiwan123";
        String password = "Secret123";
        String email = "obiwan@starwars.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";

        Instructor createdInstructor = instructorAccountService.createInstructor(username, email, password, lastName,
                firstName);

        // assertions
        assertNotNull(createdInstructor);
        assertEquals(username, createdInstructor.getUsername());
        assertEquals(password, createdInstructor.getPassword());
        assertEquals(email, createdInstructor.getEmail());
        assertEquals(firstName, createdInstructor.getFirstName());
        assertEquals(lastName, createdInstructor.getLastName());
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    public void testCreateInstructorInvalidUsername() {

        // Create instructor object params
        String username = null;
        String password = "Secret123";
        String email = "obiwan@starwars.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";

        Instructor createdInstructor = null;

        try {
            createdInstructor = instructorAccountService.createInstructor(username, email, password, lastName,
                    firstName);
        } catch (SportCenterException e) {
            assertEquals("Username cannot be blank;", e.getMessage());
        }
        assertNull(createdInstructor);
        verify(instructorRepository, times(0)).save(any(Instructor.class));
    }

    @Test
    public void testCreateInstructorUsernameTooShort() {
        // Create instructor object params
        String username = "a";
        String password = "secret";
        String email = "obiwan@starwars.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";

        Instructor createdInstructor = null;

        try {
            createdInstructor = instructorAccountService.createInstructor(username, email, password, lastName,
                    firstName);
        } catch (SportCenterException e) {
            assertEquals("Username must be at least 5 characters long;", e.getMessage());
        }
        assertNull(createdInstructor);
        verify(instructorRepository, times(0)).save(any(Instructor.class));
    }

    @Test
    public void testCreateCustomerInvalidEmail() {
        // Create instructor object params
        String username = "Obiwan123";
        String password = "secret";
        String email = "starwars@.ca";
        String firstName = "Obiwan";
        String lastName = "Kenobi";

        Instructor createdInstructor = null;

        try {
            createdInstructor = instructorAccountService.createInstructor(username, email, password, lastName,
                    firstName);
        } catch (SportCenterException e) {
            assertEquals("Email must end with @ (domain) .com;", e.getMessage());
        }
        assertNull(createdInstructor);
        verify(instructorRepository, times(0)).save(any(Instructor.class));
    }

    @Test
    public void testCreateInstructorInvalidPassword() {
        // Create instructor object params
        String username = "obiwan123";
        String password = "MissingDigit";
        String email = "obiwan@starwars.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";

        Instructor createdInstructor = null;

        try {
            createdInstructor = instructorAccountService.createInstructor(username, email, password, lastName,
                    firstName);
        } catch (SportCenterException e) {
            assertEquals("Password must contain at least one digit, one lowercase letter, and one uppercase letter;",
                    e.getMessage());
        }
        assertNull(createdInstructor);
        verify(instructorRepository, times(0)).save(any(Instructor.class));
    }

    @Test
    public void testCreateInstructorWithMultiplePasswordViolations() {
        // Create instructor object params
        String username = "obiwan123";
        String password = "MissingDigit";
        String email = "obiwan.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";

        assertThrows(SportCenterException.class, () -> {
            instructorAccountService.createInstructor(username, email, password, lastName, firstName);
        });
        verify(instructorRepository, times(0)).save(any(Instructor.class));
    }

    @Test
    public void testUpdateInstructorPassword() {
        // Create instructor object params
        String username = "obiwan123";
        String oldPassword = "Secret123";
        String newPassword = "Veryverysecret123";
        String email = "obiwan@starwars.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";
        Instructor instructor = new Instructor();
        instructor.setUsername(username);
        instructor.setPassword(oldPassword);
        instructor.setEmail(email);
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);

        when(instructorRepository.findInstructorByUsername(username)).thenReturn(instructor);
        when(instructorRepository.save(any(Instructor.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Instructor updatedInstructor = instructorAccountService.updateInstructorPassword(username, newPassword);

        // Assertions
        assertNotNull(updatedInstructor);
        assertEquals(newPassword, updatedInstructor.getPassword());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testUpdateInstructorPasswordInvalid() {
        // Create instructor object params
        String username = "obiwan123";
        String oldPassword = "Secret123";
        String newPassword = "MissingDigit";
        String email = "obiwan@starwars.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";
        Instructor instructor = new Instructor();
        instructor.setUsername(username);
        instructor.setPassword(oldPassword);
        instructor.setEmail(email);
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);

        when(instructorRepository.findInstructorByUsername(username)).thenReturn(instructor);
        when(instructorRepository.save(any(Instructor.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        try {
            instructorAccountService.updateInstructorPassword(username, newPassword);
        } catch (SportCenterException e) {
            assertEquals("Password must contain at least one digit, one lowercase letter, and one uppercase letter;",
                    e.getMessage());
        }
    }

    @Test
    public void testDeleteInstructor() {
        // Create instructor object params
        String username = "Obiwan123";
        String password = "secret";
        String email = "obiwan@starwars.com";
        String firstName = "Obiwan";
        String lastName = "Kenobi";
        Instructor instructor = new Instructor();
        instructor.setUsername(username);
        instructor.setPassword(password);
        instructor.setEmail(email);
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);

        when(instructorRepository.findInstructorByUsername(username)).thenReturn(instructor);

        instructorAccountService.deleteInstructor(username);

        verify(instructorRepository, times(1)).delete(instructor);
    }
}
