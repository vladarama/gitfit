package ca.mcgill.ecse321.gitfit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Time;

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

import ca.mcgill.ecse321.gitfit.dao.OwnerRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.dto.OwnerAccountDto;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OwnerAccountIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    private final String SPORTCENTER_NAME = "McGill Gym";
    private final int SPORTCENTER_CAPACITY = 50;
    private final Time SPORTCENTER_OPENING_TIME = Time.valueOf("07:00:00");
    private final Time SPORTCENTER_CLOSING_TIME = Time.valueOf("21:00:00");
    private static final String USERNAME = "AnakinTEST";
    private static final String EMAIL = "anakin@starwars.com";
    private static final String PASSWORD = "theForce123";
    private static final String LAST_NAME = "Skywalker";
    private static final String FIRST_NAME = "Anakin";

    @BeforeAll
    public void setup() {
        ownerRepository.deleteAll();
        sportCenterRepository.deleteAll();
        SportCenter sportCenter = new SportCenter(SPORTCENTER_NAME, SPORTCENTER_CAPACITY, SPORTCENTER_OPENING_TIME,
                SPORTCENTER_CLOSING_TIME, USERNAME, EMAIL, PASSWORD, LAST_NAME, FIRST_NAME);
        sportCenterRepository.save(sportCenter);
        ownerRepository.save(sportCenter.getOwner());
    }

    @AfterAll
    public void clearDatabase() {
        ownerRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testGetOwner() {
        ResponseEntity<OwnerAccountDto> response = client.getForEntity("/owner", OwnerAccountDto.class);
        assertNotNull(response);
        OwnerAccountDto owner = response.getBody();
        assertNotNull(owner);
        assertEquals(USERNAME, owner.getUsername());
        assertEquals(EMAIL, owner.getEmail());
        assertEquals(FIRST_NAME, owner.getFirstName());
        assertEquals(LAST_NAME, owner.getLastName());
    }

    @Test
    @Order(2)
    public void testUpdateOwnerPassword() {
        String newPassword = "theForce456";

        // setup
        HttpEntity<String> request = new HttpEntity<>(newPassword);

        // Act
        ResponseEntity<OwnerAccountDto> response = client.exchange("/owner/Password",
                HttpMethod.PUT, request,
                OwnerAccountDto.class);

        assertNotNull(response);
        OwnerAccountDto owner = response.getBody();
        assertNotNull(owner);
        assertEquals(USERNAME, owner.getUsername());
        assertEquals(newPassword, owner.getPassword());
    }

    @Test
    @Order(3)
    public void testUpdateOwnerPasswordInvalid() {
        String newPassword = "123";

        // setup
        HttpEntity<String> request = new HttpEntity<>(newPassword);

        // Act
        ResponseEntity<String> response = client.exchange("/owner/Password",
                HttpMethod.PUT, request, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Password must be at least 8 characters long;"));
    }

}
