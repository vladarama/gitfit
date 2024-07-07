package ca.mcgill.ecse321.gitfit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.gitfit.dao.BillingRepository;
import ca.mcgill.ecse321.gitfit.dao.CustomerRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.dto.BillingRequestDto;
import ca.mcgill.ecse321.gitfit.dto.BillingResponseDto;
import ca.mcgill.ecse321.gitfit.dto.ErrorDto;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class BillingIntegrationTests {
    @Autowired
    private TestRestTemplate client;
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;

    private final String VALID_COUNTRY = "Canada";
    private final String VALID_STATE = "Quebec";
    private final String VALID_POSTAL_CODE = "H3H1P3";
    private final String VALID_CARD_NUMBER = "8888888888888888";
    private final String VALID_ADDRESS = "1234 Rue Sherbrooke";
    private final String VALID_USERNAME = "Bob";
    private final String CUSTOMER_WITHOUT_BILLING = "customerWithoutBilling";

    @BeforeAll
    public void setUpCustomers() {
        billingRepository.deleteAll();
        customerRepository.deleteAll();
        sportCenterRepository.deleteAll();

        SportCenter sportCenter = new SportCenter();
        sportCenter = sportCenterRepository.save(sportCenter);
        Customer customer = new Customer();
        customer.setUsername(VALID_USERNAME);
        customer.setSportCenter(sportCenter);
        customerRepository.save(customer);

        Customer customerWithoutBilling = new Customer();
        customerWithoutBilling.setUsername(CUSTOMER_WITHOUT_BILLING);
        customerWithoutBilling.setSportCenter(sportCenter);
        customerRepository.save(customerWithoutBilling);
    }

    @AfterAll
    public void clearDatabase() {
        billingRepository.deleteAll();
        customerRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateNonExistingCustomerBilling() {
        BillingRequestDto billingRequestDto = new BillingRequestDto(VALID_COUNTRY, VALID_STATE, VALID_POSTAL_CODE,
                VALID_CARD_NUMBER, VALID_ADDRESS, "NonExistingCustomer");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(billingRequestDto, headers);
        ResponseEntity<ErrorDto> response = client.exchange("/customers/NonExistingCustomer/billing", HttpMethod.PUT,
                entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The customer does not exist.", body.getErrors().get(0));

    }

    @Test
    @Order(2)
    public void testCreateIncompleteFieldBilling() {
        BillingRequestDto billingRequestDto = new BillingRequestDto(null, VALID_STATE, VALID_POSTAL_CODE,
                VALID_CARD_NUMBER, VALID_ADDRESS, VALID_USERNAME);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(billingRequestDto, headers);
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + VALID_USERNAME + "/billing", HttpMethod.PUT,
                entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The billing information fields must be completed.", body.getErrors().get(0));
    }

    @Test
    @Order(3)
    public void testCreateInvalidPostalCodeBilling() {
        BillingRequestDto billingRequestDto = new BillingRequestDto(VALID_COUNTRY, VALID_STATE, "H3HP",
                VALID_CARD_NUMBER, VALID_ADDRESS, VALID_USERNAME);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(billingRequestDto, headers);
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + VALID_USERNAME + "/billing", HttpMethod.PUT,
                entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("Postal code must be between 5 and 10 alphanumeric characters", body.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testCreateValidBilling() {
        BillingRequestDto billingRequestDto = new BillingRequestDto(VALID_COUNTRY, VALID_STATE, VALID_POSTAL_CODE,
                VALID_CARD_NUMBER, VALID_ADDRESS, VALID_USERNAME);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(billingRequestDto, headers);
        ResponseEntity<BillingResponseDto> response = client.exchange("/customers/" + VALID_USERNAME + "/billing",
                HttpMethod.PUT, entity, BillingResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BillingResponseDto createdBilling = response.getBody();
        assertNotNull(createdBilling);
        assertEquals(VALID_COUNTRY, createdBilling.getCountry());
        assertEquals(VALID_STATE, createdBilling.getState());
        assertEquals(VALID_POSTAL_CODE, createdBilling.getPostalCode());
        assertEquals("8888", createdBilling.getCardNumberEnd());
    }

    @Test
    @Order(5)
    public void testGetNonExistingCustomerBilling() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = client.exchange("/customers/NonExistingCustomer/billing", HttpMethod.GET,
                entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The customer does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(6)
    public void testGetNonExistingBilling() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + CUSTOMER_WITHOUT_BILLING + "/billing",
                HttpMethod.GET, entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The customer does not have billing set up.", body.getErrors().get(0));
    }

    @Test
    @Order(7)
    public void testGetBilling() {
        ResponseEntity<BillingResponseDto> response = client.getForEntity("/customers/" + VALID_USERNAME + "/billing",
                BillingResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BillingResponseDto createdBilling = response.getBody();
        assertNotNull(createdBilling);
        assertEquals(VALID_COUNTRY, createdBilling.getCountry());
        assertEquals(VALID_STATE, createdBilling.getState());
        assertEquals(VALID_POSTAL_CODE, createdBilling.getPostalCode());
        assertEquals("8888", createdBilling.getCardNumberEnd());
    }

    @Test
    @Order(8)
    public void testDeleteNonExistingCustomerBilling() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = client.exchange("/customers/NonExistingCustomer/billing", HttpMethod.DELETE,
                entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The customer does not exist.", body.getErrors().get(0));
    }

    @Test
    @Order(9)
    public void testDeleteNonExistingBilling() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = client.exchange("/customers/" + CUSTOMER_WITHOUT_BILLING + "/billing",
                HttpMethod.DELETE, entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getErrors().size());
        assertEquals("The customer does not have billing set up.", body.getErrors().get(0));
    }

    @Test
    @Order(10)
    public void testDeleteBilling() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BillingRequestDto> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = client.exchange("/customers/" + VALID_USERNAME + "/billing", HttpMethod.DELETE,
                entity, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}