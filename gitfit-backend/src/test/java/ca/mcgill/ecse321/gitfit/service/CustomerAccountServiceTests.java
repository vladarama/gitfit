package ca.mcgill.ecse321.gitfit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.SportCenter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest
public class CustomerAccountServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private ValidatorService validatorService;

    @Mock
    private SportCenterService sportCenterService;

    @InjectMocks
    private CustomerAccountService customerAccountService;

    private static final String CUSTOMER_KEY = "TestCustomer";
    private static final String NONEXISTING_KEY = "NotACustomer";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(customerRepository.findCustomerByUsername(anyString()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(CUSTOMER_KEY)) {
                        Customer customer = new Customer();
                        customer.setUsername(CUSTOMER_KEY);
                        return customer;
                    } else {
                        return null;
                    }
                });
        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(customerRepository.save(any(Customer.class))).thenAnswer(returnParameterAsAnswer);

        SportCenter mockSportCenter = new SportCenter();
        when(sportCenterService.getSportCenter()).thenReturn(mockSportCenter);

        // Mock the ValidatorService
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        doAnswer(invocation -> {
            Object argument = invocation.getArgument(0);
            Set<ConstraintViolation<Object>> violations = validator.validate(argument);
            if (!violations.isEmpty()) {
                ConstraintViolation<?> firstViolation = violations.iterator().next();
                throw new SportCenterException(HttpStatus.BAD_REQUEST,
                        firstViolation.getMessage());
            }
            return null;
        }).when(validatorService).validate(any());

    }

    @Test
    public void testGetCustomer() {
        assertEquals(CUSTOMER_KEY, customerAccountService.getCustomer(CUSTOMER_KEY).getUsername());
    }

    @Test
    public void testGetCustomerNotFound() {
        Exception exception = assertThrows(SportCenterException.class, () -> {
            customerAccountService.getCustomer(NONEXISTING_KEY);
        });

        String expectedMessage = "Customer not found.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetAllCustomers() {

        Customer customer1 = new Customer();
        Customer customer2 = new Customer();

        List<Customer> mockCustomers = Arrays.asList(customer1, customer2);
        lenient().when(customerRepository.findAll()).thenReturn(mockCustomers);

        List<Customer> retrievedCustomers = customerAccountService.getAllCustomers();

        assertNotNull(retrievedCustomers);
        assertEquals(mockCustomers.size(), retrievedCustomers.size());
        assertTrue(retrievedCustomers.contains(customer1));
        assertTrue(retrievedCustomers.contains(customer2));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testGetAllCustomersNoCustomersFound() {

        lenient().when(customerRepository.findAll()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(SportCenterException.class, () -> {
            customerAccountService.getAllCustomers();
        });

        String expectedMessage = "No current customers.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testCreateCustomerWithBillingInfo() {

        String username = "kirkaWithBillingInfo";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (IllegalArgumentException e) {
            // Check that no error occurred
            fail();
        }
        assertNotNull(customer);
        assertEquals(username, customer.getUsername());
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(lastName, customer.getLastName());
        assertEquals(firstName, customer.getFirstName());
        assertNotNull(customer.getBilling());
        assertEquals(country, customer.getBilling().getCountry());
        assertEquals(state, customer.getBilling().getState());
        assertEquals(postalCode, customer.getBilling().getPostalCode());
        assertEquals(cardNumber, customer.getBilling().getCardNumber());
        assertEquals(address, customer.getBilling().getAddress());
    }

    @Test
    public void testCreateCustomerEmpty() {
        String username = "kirka";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;
        try {
            customer = customerAccountService.createCustomer(username, email, password,
                    lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Last name cannot be blank";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithSpace() {
        String username = "kirka";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "         ";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;
        try {
            customer = customerAccountService.createCustomer(username, email, password,
                    lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "First name cannot be blank";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithoutBillingInfo() {

        String username = "kirkaNoBillingInfo";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = null;
        String state = null;
        String postalCode = null;
        String cardNumber = null;
        String address = null;
        Customer customer = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (IllegalArgumentException e) {
            // Check that no error occurred
            fail();
        }
        assertNotNull(customer);
        assertEquals(username, customer.getUsername());
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(lastName, customer.getLastName());
        assertEquals(firstName, customer.getFirstName());
        assertNull(customer.getBilling());
    }

    @Test
    public void testCreateCustomerWithIncompleteBillingInfo() {

        String username = "kirkaIncompleteBillingInfo";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = null;
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (IllegalArgumentException e) {
            // Check that no error occurred
            fail();
        }
        assertNotNull(customer);
        assertEquals(username, customer.getUsername());
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(lastName, customer.getLastName());
        assertEquals(firstName, customer.getFirstName());
        assertNull(customer.getBilling());
    }

    @Test
    public void testCreateCustomerWithEmptyBillingInfo() {

        String username = "kirkaEmptyBillingInfo";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (IllegalArgumentException e) {
            // Check that no error occurred
            fail();
        }
        assertNotNull(customer);
        assertEquals(username, customer.getUsername());
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(lastName, customer.getLastName());
        assertEquals(firstName, customer.getFirstName());
        assertNull(customer.getBilling());
    }

    @Test
    public void testCreateCustomerBillingInfoWithSpaces() {

        String username = "kirkaBillingInfowithSpaces";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "            ";
        Customer customer = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (IllegalArgumentException e) {
            // Check that no error occurred
            fail();
        }
        assertNotNull(customer);
        assertEquals(username, customer.getUsername());
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(lastName, customer.getLastName());
        assertEquals(firstName, customer.getFirstName());
        assertNull(customer.getBilling());
    }

    @Test
    public void testCreateCustomerWithNullUsername() {
        String username = null;
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;
        try {
            customer = customerAccountService.createCustomer(username, email, password,
                    lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Username cannot be blank";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithShortUsername() {

        String username = "kirk";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Username must be at least 5 characters long";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithNullPassword() {

        String username = "kirka";
        String email = "kirka@example.com";
        String password = null;
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Password cannot be Blank";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithIncorrectPassword() {

        String username = "kirka";
        String email = "kirka@example.com";
        String password = "password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Password must contain at least one digit, one lowercase letter, and one uppercase letter";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithShortPassword() {

        String username = "kirka";
        String email = "kirka@example.com";
        String password = "Pass1";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Password must be at least 8 characters long";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithEmptyPassword() {

        String username = "kirka";
        String email = "kirka@example.com";
        String password = "";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";

        assertThrows(SportCenterException.class, () -> {
            customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        });

        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomerWithNullEmail() {
        String username = "kirka";
        String email = null;
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;
        try {
            customer = customerAccountService.createCustomer(username, email, password,
                    lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Email cannot be blank";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithIncorrectEmail() {

        String username = "kirka";
        String email = "kirka@example.ca";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Email must end with @ (domain) .com";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithNullFirstName() {
        String username = "kirka";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = null;
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;
        try {
            customer = customerAccountService.createCustomer(username, email, password,
                    lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "First name cannot be blank";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithNullLastName() {
        String username = "kirka";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = null;
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;
        try {
            customer = customerAccountService.createCustomer(username, email, password,
                    lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Last name cannot be blank";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithIncorrectBillingPostalCode() {

        String username = "kirkaIncorrectPostalCode";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A";
        String cardNumber = "1234567890123456";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Postal code must be between 5 and 10 alphanumeric characters";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testCreateCustomerWithIncorrectBillingCardNumber() {

        String username = "kirkaIncorrectCardNumber";
        String email = "kirka@example.com";
        String password = "Password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 1B1";
        String cardNumber = "123456789012345";
        String address = "845 Sherbrooke St W, Montreal";
        Customer customer = null;
        String error = null;

        try {
            customer = customerAccountService.createCustomer(username, email, password, lastName,
                    firstName, country, state, postalCode, cardNumber, address);
        } catch (Exception e) {
            error = e.getMessage();
        }

        assertNull(customer);
        String expectedErrorMessage = "Card number must be 16 digits";
        assertEquals(expectedErrorMessage, error);
    }

    @Test
    public void testUpdateCustomerPasswordSuccess() {
        String username = "kirka";
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword123";
        Customer mockCustomer = new Customer();
        mockCustomer.setUsername(username);
        mockCustomer.setPassword(oldPassword);

        when(customerRepository.findCustomerByUsername(username)).thenReturn(mockCustomer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer updatedCustomer = customerAccountService.updateCustomerPassword(username, newPassword);

        assertNotNull(updatedCustomer);
        assertEquals(newPassword, updatedCustomer.getPassword());
    }

    @Test
    public void testUpdatePasswordForNonExistingCustomer() {
        String nonExistingUsername = "nonExistingUser";
        String newPassword = "newPassword123";

        Exception exception = assertThrows(SportCenterException.class, () -> {
            customerAccountService.updateCustomerPassword(nonExistingUsername, newPassword);
        });

        String expectedMessage = "Customer not found.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testUpdateCustomerPasswordWithNullPassword() {
        String username = "kirka";
        String oldPassword = "oldPassword123";
        String newPassword = null;

        Customer mockCustomer = new Customer();
        mockCustomer.setUsername(username);
        mockCustomer.setPassword(oldPassword);

        when(customerRepository.findCustomerByUsername(username)).thenReturn(mockCustomer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Exception exception = assertThrows(SportCenterException.class, () -> {
            customerAccountService.updateCustomerPassword(username, newPassword);
        });

        String expectedMessage = "Password cannot be Blank";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testUpdateCustomerPasswordWithShortPassword() {
        String username = "kirka";
        String oldPassword = "oldPassword123";
        String newPassword = "New1";

        Customer mockCustomer = new Customer();
        mockCustomer.setUsername(username);
        mockCustomer.setPassword(oldPassword);

        when(customerRepository.findCustomerByUsername(username)).thenReturn(mockCustomer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Exception exception = assertThrows(SportCenterException.class, () -> {
            customerAccountService.updateCustomerPassword(username, newPassword);
        });

        String expectedMessage = "Password must be at least 8 characters long";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testUpdateCustomerPasswordWithEmptyPassword() {
        String username = "kirka";
        String oldPassword = "oldPassword123";
        String newPassword = "";

        Customer mockCustomer = new Customer();
        mockCustomer.setUsername(username);
        mockCustomer.setPassword(oldPassword);

        when(customerRepository.findCustomerByUsername(username)).thenReturn(mockCustomer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(SportCenterException.class, () -> {
            customerAccountService.updateCustomerPassword(username, newPassword);
        });

        verify(customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void testUpdateCustomerPasswordWithIncorrectPassword() {
        String username = "kirka";
        String oldPassword = "oldPassword123";
        String newPassword = "Newwwwwww";

        Customer mockCustomer = new Customer();
        mockCustomer.setUsername(username);
        mockCustomer.setPassword(oldPassword);

        when(customerRepository.findCustomerByUsername(username)).thenReturn(mockCustomer);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Exception exception = assertThrows(SportCenterException.class, () -> {
            customerAccountService.updateCustomerPassword(username, newPassword);
        });

        String expectedMessage = "Password must contain at least one digit, one lowercase letter, and one uppercase letter";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testDeleteCustomer() {
        SportCenter sportCenter = new SportCenter();

        String username = "kirka123";
        String password = "secret";
        String email = "kirka@gmail.com";
        String firstName = "Kliment";
        String lastName = "Kirk";
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setSportCenter(sportCenter);

        when(customerRepository.findCustomerByUsername(username)).thenReturn(customer);

        customerAccountService.deleteCustomer(username);

        verify(customerRepository, times(1)).delete(customer);
    }
}
