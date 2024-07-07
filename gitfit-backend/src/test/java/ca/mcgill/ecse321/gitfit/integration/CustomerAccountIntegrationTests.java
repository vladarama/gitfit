package ca.mcgill.ecse321.gitfit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.gitfit.dao.CustomerRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.dto.CustomerAccountDto;
import ca.mcgill.ecse321.gitfit.dto.CustomerAccountRequestDto;
import ca.mcgill.ecse321.gitfit.dto.PasswordRequestDto;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerAccountIntegrationTests {

        @Autowired
        private TestRestTemplate client;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private SportCenterRepository sportCenterRepository;

        private static final String USERNAME = "AnakinCustomer";
        private static final String EMAIL = "anakin@starwars.com";
        private static final String PASSWORD = "theForce123";
        private static final String LAST_NAME = "Skywalker";
        private static final String FIRST_NAME = "Anakin";
        private static final SportCenter SPORT_CENTER = new SportCenter();

        @BeforeAll
        public void setup() {
                customerRepository.deleteAll();
                sportCenterRepository.deleteAll();
                sportCenterRepository.save(SPORT_CENTER);
                customerRepository.save(new Customer(USERNAME, EMAIL, PASSWORD, LAST_NAME, FIRST_NAME, SPORT_CENTER));
        }

        @AfterAll
        public void clearDatabase() {
                customerRepository.deleteAll();
                sportCenterRepository.deleteAll();
        }

        @Test
        @Order(1)
        public void testGetCustomer() {
                ResponseEntity<CustomerAccountDto> response = client.getForEntity("/customer/" + USERNAME,
                                CustomerAccountDto.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                CustomerAccountDto customer = response.getBody();
                assertNotNull(customer);
                assertEquals(USERNAME, customer.getUsername());
                assertEquals(EMAIL, customer.getEmail());
                assertEquals(FIRST_NAME, customer.getFirstName());
                assertEquals(LAST_NAME, customer.getLastName());
                assertEquals(PASSWORD, customer.getPassword());

        }

        @Test
        @Order(2)
        public void testGetInvalidCustomer() {
                String UNKNOWN_USERNAME = "000000";
                ResponseEntity<CustomerAccountDto> response = client.getForEntity("/customer/" + UNKNOWN_USERNAME,
                                CustomerAccountDto.class);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @Order(3)
        public void testGetAllCustomers() {
                ResponseEntity<CustomerAccountDto[]> response = client.getForEntity("/customers/",
                                CustomerAccountDto[].class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                CustomerAccountDto[] customers = response.getBody();
                assertNotNull(customers);
                assertEquals(1, customers.length);
        }

        @Test
        @Order(4)
        public void testCreateValidCustomer() {
                String newUsername = "kirkaluz";
                String newEmail = "klimentkirk@gmail.com";
                String newPassword = "Password123";
                String newLastName = "Kirk";
                String newFirstName = "Kliment";
                String newCountry = "Canada";
                String newState = "Quebec";
                String newPostalCode = "H3A 1B1";
                String newCardNumber = "1234567890123456";
                String newAddress = "845 Sherbrooke St W, Montreal";

                CustomerAccountRequestDto customerAccountRequestDto = new CustomerAccountRequestDto(newUsername,
                                newEmail,
                                newPassword,
                                newLastName,
                                newFirstName, newCountry, newState, newPostalCode,
                                newCardNumber, newAddress);

                HttpEntity<CustomerAccountRequestDto> request = new HttpEntity<>(customerAccountRequestDto);

                ResponseEntity<CustomerAccountDto> response = client.postForEntity(
                                "/customer/",
                                request,
                                CustomerAccountDto.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                CustomerAccountDto customer = response.getBody();
                assertNotNull(customer);
                assertEquals(newUsername, customer.getUsername());
                assertEquals(newEmail, customer.getEmail());
                assertEquals(newFirstName, customer.getFirstName());
                assertEquals(newLastName, customer.getLastName());
                assertEquals(newPassword, customer.getPassword());
                assertEquals(newPassword, customer.getPassword());

                ResponseEntity<CustomerAccountDto[]> response2 = client.getForEntity("/customers/",
                                CustomerAccountDto[].class);
                CustomerAccountDto[] customers = response2.getBody();
                assertEquals(2, customers.length);
        }

        @Test
        @Order(5)
        public void testCreateInvalidCustomerWrongPassword() {
                String newUsername = "kliment";
                String newEmail = "luke@starwars.com";
                String newPassword = "badPassword";
                String newLastName = "Skywalker";
                String newFirstName = "Luke";
                String newCountry = "Canada";
                String newState = "Quebec";
                String newPostalCode = "H3A 1B1";
                String newCardNumber = "1234567890123456";
                String newAddress = "845 Sherbrooke St W, Montreal";

                CustomerAccountRequestDto customerAccountRequestDto = new CustomerAccountRequestDto(newUsername,
                                newEmail,
                                newPassword,
                                newLastName,
                                newFirstName, newCountry, newState, newPostalCode,
                                newCardNumber, newAddress);

                HttpEntity<CustomerAccountRequestDto> request = new HttpEntity<>(customerAccountRequestDto);

                ResponseEntity<String> response = client.postForEntity(
                                "/customer/",
                                request,
                                String.class);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertTrue(response.getBody()
                                .contains("Password must contain at least one digit, one lowercase letter, and one uppercase letter;"));

                ResponseEntity<CustomerAccountDto[]> response2 = client.getForEntity("/customers/",
                                CustomerAccountDto[].class);
                CustomerAccountDto[] customers = response2.getBody();
                assertEquals(2, customers.length);
        }

        @Test
        @Order(6)
        public void testCreateInvalidCustomerExistingUsername() {
                String newEmail = "luke@starwars.com";
                String newPassword = "LukeForce456878";
                String newLastName = "Skywalker";
                String newFirstName = "Luke";
                String newCountry = "Canada";
                String newState = "Quebec";
                String newPostalCode = "H3A 1B1";
                String newCardNumber = "1234567890123456";
                String newAddress = "845 Sherbrooke St W, Montreal";

                CustomerAccountRequestDto customerAccountRequestDto = new CustomerAccountRequestDto(USERNAME, newEmail,
                                newPassword,
                                newLastName,
                                newFirstName, newCountry, newState, newPostalCode,
                                newCardNumber, newAddress);

                HttpEntity<CustomerAccountRequestDto> request = new HttpEntity<>(customerAccountRequestDto);

                ResponseEntity<String> response = client.postForEntity(
                                "/customer/",
                                request,
                                String.class);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertTrue(response.getBody()
                                .contains("Username already exists."));

                ResponseEntity<CustomerAccountDto[]> response2 = client.getForEntity("/customers/",
                                CustomerAccountDto[].class);
                CustomerAccountDto[] customers = response2.getBody();
                assertEquals(2, customers.length);
        }

        @Test
        @Order(7)
        public void testUpdateCustomerPassword() {
                String newPassword = "LukeForce456878";

                PasswordRequestDto passwordRequestDto = new PasswordRequestDto(USERNAME, newPassword);
                HttpEntity<PasswordRequestDto> request = new HttpEntity<>(passwordRequestDto);

                ResponseEntity<String> response = client.exchange(
                                "/customer/password/",
                                HttpMethod.PUT,
                                request,
                                String.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(newPassword, customerRepository.findCustomerByUsername(USERNAME).getPassword());
        }

        @Test
        @Order(8)
        public void testUpdateInvalidCustomerPassword() {
                String newPassword = "   ";

                PasswordRequestDto passwordRequestDto = new PasswordRequestDto(USERNAME, newPassword);
                HttpEntity<PasswordRequestDto> request = new HttpEntity<>(passwordRequestDto);

                ResponseEntity<String> response = client.exchange(
                                "/customer/password/",
                                HttpMethod.PUT,
                                request,
                                String.class);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertTrue(response.getBody()
                                .contains("Password cannot be Blank;"));
        }

        @Test
        @Order(9)
        public void testDeleteCustomer() {
                HttpEntity<String> request = new HttpEntity<>(USERNAME);

                ResponseEntity<String> response = client.exchange(
                                "/customer/",
                                HttpMethod.DELETE,
                                request,
                                String.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(1, customerRepository.count());
        }
}