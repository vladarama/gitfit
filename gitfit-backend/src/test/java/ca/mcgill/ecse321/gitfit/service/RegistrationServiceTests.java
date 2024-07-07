package ca.mcgill.ecse321.gitfit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.gitfit.dao.RegistrationRepository;
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class RegistrationServiceTests {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private CustomerAccountService customerService;

    @Mock
    private SportCenterService sportCenterService;

    @InjectMocks
    private RegistrationService registrationService;

    // Sessions
    private Session session1 = new Session();
    private Session session2 = new Session();
    private Session session3 = new Session();

    // Customers
    private Customer customer1 = new Customer();
    private Customer customer2 = new Customer();
    private Customer customer3 = new Customer();

    // SportCenter instance
    private SportCenter sportCenter = new SportCenter();

    // Registrations
    private Registration registration1 = new Registration();
    private Registration registration2 = new Registration();
    private Registration registration3 = new Registration();

    @BeforeEach
    public void setMockOutput() {
        // Initializing sessions
        session1.setId(1);
        session2.setId(2);
        session3.setId(3);

        // Initializing customers with unique usernames
        customer1.setUsername("user1");
        customer2.setUsername("user2");
        customer3.setUsername("user3");

        // Configuring each registration with unique attributes
        configureRegistration(registration1, 1, Date.valueOf("2024-11-15"), session1,
                customer1);
        configureRegistration(registration2, 2, Date.valueOf("2024-12-15"), session2,
                customer2);
        configureRegistration(registration3, 3, Date.valueOf("2025-01-15"), session3,
                customer3);

        // Mocking repository behaviors
        when(registrationRepository.findRegistrationById(1)).thenReturn(registration1);
        when(registrationRepository.findRegistrationById(2)).thenReturn(registration2);
        when(registrationRepository.findRegistrationById(3)).thenReturn(registration3);
        when(registrationRepository.findAll()).thenReturn(Arrays.asList(registration1,
                registration2, registration3));
        when(sessionRepository.findSessionById(anyInt())).thenAnswer(i -> {
            int id = i.getArgument(0);
            switch (id) {
                case 1:
                    return session1;
                case 2:
                    return session2;
                case 3:
                    return session3;
                default:
                    return null;
            }
        });
        when(customerService.getCustomer(anyString())).thenAnswer(i -> {
            String username = i.getArgument(0);
            switch (username) {
                case "user1":
                    return customer1;
                case "user2":
                    return customer2;
                case "user3":
                    return customer3;
                default:
                    return null;
            }
        });
        when(sportCenterService.getSportCenter()).thenReturn(sportCenter);
    }

    private void configureRegistration(Registration registration, int id, Date date, Session session,
            Customer customer) {
        registration.setId(id);
        registration.setDate(date);
        registration.setSession(session);
        registration.setCustomer(customer);
        registration.setSportCenter(sportCenter);
    }

    @Test
    public void testGetRegistrationByIdSuccessful() {
        when(registrationRepository.findRegistrationById(1)).thenReturn(registration1);
        Registration foundRegistration = registrationService.getRegistration(1);
        assertNotNull(foundRegistration, "The found registration should not be null.");
        assertEquals(1, foundRegistration.getId(), "The ID of the found registration should match the requested ID.");
        assertEquals(registration1, foundRegistration,
                "The found registration should match the expected registration object.");
    }

    @Test
    public void testGetRegistrationByIdNotFound() {
        when(registrationRepository.findRegistrationById(999)).thenReturn(null);
        Exception exception = assertThrows(SportCenterException.class, () -> {
            registrationService.getRegistration(999);
        }, "A SportCenterException should be thrown if the registration is not found.");
        assertEquals(HttpStatus.NOT_FOUND, ((SportCenterException) exception).getStatus(),
                "The status should be NOT_FOUND for a non-existent registration.");
        assertEquals("Registration not found.", exception.getMessage(),
                "The exception message should indicate that the registration was not found.");
    }

    @Test
    public void testGetAllRegistrations() {
        List<Registration> registrations = registrationService.getAllRegistrations();
        assertNotNull(registrations);
        assertEquals(3, registrations.size());
    }

    @Test
    public void testCreateRegistrationSuccessful() {
        Registration newRegistration = registrationService.createRegistration(Date.valueOf("2024-12-20"), 1,
                "user1");
        assertNotNull(newRegistration);
        assertEquals(Date.valueOf("2024-12-20"), newRegistration.getDate());
        assertEquals(1, newRegistration.getSession().getId());
        assertEquals("user1", newRegistration.getCustomer().getUsername());
    }

    @Test
    public void testCreateRegistrationWithNonexistentSession() {
        Exception exception = assertThrows(SportCenterException.class, () -> {
            registrationService.createRegistration(Date.valueOf("2024-12-25"), 999,
                    "user1"); // Non-existent session ID
        });
        assertEquals(HttpStatus.NOT_FOUND, ((SportCenterException) exception).getStatus());
    }

    @Test
    public void testCreateRegistrationWithNonexistentCustomer() {
        Exception exception = assertThrows(SportCenterException.class, () -> {
            registrationService.createRegistration(Date.valueOf("2024-12-30"), 1,
                    "nonexistentUser"); // Non-existent
            // user
        });
        assertEquals(HttpStatus.NOT_FOUND, ((SportCenterException) exception).getStatus());
    }

    @Test
    public void testDeleteRegistrationSuccessful() {
        assertTrue(registrationService.deleteRegistration(1));
        verify(registrationRepository, times(1)).delete(registration1);
    }

    @Test
    public void testDeleteNonexistentRegistration() {
        lenient().when(registrationRepository.findRegistrationById(999)).thenReturn(null);
        Exception exception = assertThrows(SportCenterException.class,
                () -> registrationService.deleteRegistration(999));
        assertEquals(HttpStatus.NOT_FOUND, ((SportCenterException) exception).getStatus());
        verify(registrationRepository, never()).delete(any(Registration.class));
    }

    @Test
    public void testGetAllCustomerRegistrationsForNonexistentUser() {
        Exception exception = assertThrows(SportCenterException.class, () -> {
            registrationService.getAllCustomerRegistrations("nonexistentUser");
        });
        assertEquals(HttpStatus.NOT_FOUND, ((SportCenterException) exception).getStatus());
    }

    @Test
    public void testGetAllSessionRegistrations() {
        List<Registration> registrationsForSession1 = registrationService.getAllSessionRegistrations(1);
        assertEquals(1, registrationsForSession1.size());
        assertEquals(1, registrationsForSession1.get(0).getSession().getId());

        List<Registration> registrationsForSession2 = registrationService.getAllSessionRegistrations(2);
        assertEquals(1, registrationsForSession2.size());
        assertEquals(2, registrationsForSession2.get(0).getSession().getId());
    }

    @Test
    public void testGetAllSessionRegistrationsForNonexistentSession() {
        Exception exception = assertThrows(SportCenterException.class, () -> {
            registrationService.getAllSessionRegistrations(999);
        });
        assertEquals(HttpStatus.NOT_FOUND, ((SportCenterException) exception).getStatus());
    }

}