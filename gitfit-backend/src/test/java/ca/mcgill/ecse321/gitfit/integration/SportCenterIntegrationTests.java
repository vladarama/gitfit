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

import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.dto.ErrorDto;
import ca.mcgill.ecse321.gitfit.dto.HoursDto;
import ca.mcgill.ecse321.gitfit.dto.SportCenterDto;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SportCenterIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    private final String SPORTCENTER_NAME = "McGill Gym";
    private final int SPORTCENTER_CAPACITY = 50;
    private final Time SPORTCENTER_OPENING_TIME = Time.valueOf("07:00:00");
    private final Time SPORTCENTER_CLOSING_TIME = Time.valueOf("21:00:00");

    private final String VALID_SPORTCENTER_NAME = "GitFit";
    private final int VALID_SPORTCENTER_CAPACITY = 100;
    private final Time VALID_SPORTCENTER_OPENING_TIME = Time.valueOf("08:00:00");
    private final Time VALID_SPORTCENTER_CLOSING_TIME = Time.valueOf("20:00:00");

    private final String INVALID_SPORTCENTER_NAME = "     ";
    private final int INVALID_SPORTCENTER_CAPACITY = -1;
    private final Time INVALID_SPORTCENTER_OPENING_TIME = Time.valueOf("08:00:00");
    private final Time INVALID_SPORTCENTER_CLOSING_TIME = Time.valueOf("07:00:00");

    @BeforeAll
    public void setup() {
        sportCenterRepository.deleteAll();

        SportCenter sportCenter = new SportCenter();
        sportCenter.setName(SPORTCENTER_NAME);
        sportCenter.setMaxCapacity(SPORTCENTER_CAPACITY);
        sportCenter.setOpeningTime(SPORTCENTER_OPENING_TIME);
        sportCenter.setClosingTime(SPORTCENTER_CLOSING_TIME);
        sportCenterRepository.save(sportCenter);
    }

    @AfterAll
    public void clearDatabase() {
        sportCenterRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testGetSportCenter() {
        // Act
        ResponseEntity<SportCenterDto> response = client.getForEntity("/sportcenter", SportCenterDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SportCenterDto sportCenter = response.getBody();
        assertNotNull(sportCenter);
        assertEquals(SPORTCENTER_NAME, sportCenter.getName());
        assertEquals(SPORTCENTER_CAPACITY, sportCenter.getMaxCapacity());
        assertEquals(SPORTCENTER_OPENING_TIME, Time.valueOf(sportCenter.getOpeningTime()));
        assertEquals(SPORTCENTER_CLOSING_TIME, Time.valueOf(sportCenter.getClosingTime()));
    }

    @Test
    @Order(2)
    public void testUpdateSportCenterName() {
        // Setup
        HttpEntity<String> request = new HttpEntity<>(VALID_SPORTCENTER_NAME);

        // Act
        ResponseEntity<SportCenterDto> response = client.exchange("/sportcenter/name", HttpMethod.PUT, request,
                SportCenterDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SportCenterDto sportCenter = response.getBody();
        assertNotNull(sportCenter);
        assertEquals(VALID_SPORTCENTER_NAME, sportCenter.getName());
    }

    @Test
    @Order(3)
    public void testUpdateSportCenterNameInvalid() {
        // Setup
        HttpEntity<String> request = new HttpEntity<>(INVALID_SPORTCENTER_NAME);

        // Act
        ResponseEntity<ErrorDto> response = client.exchange("/sportcenter/name", HttpMethod.PUT, request,
                ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertTrue(error.getErrors().contains("Name cannot be null or empty"));
    }

    @Test
    public void testUpdateSportCenterMaxCapacity() {
        // Setup
        HttpEntity<Integer> request = new HttpEntity<>(VALID_SPORTCENTER_CAPACITY);

        // Act
        ResponseEntity<SportCenterDto> response = client.exchange("/sportcenter/capacity", HttpMethod.PUT, request,
                SportCenterDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SportCenterDto sportCenter = response.getBody();
        assertNotNull(sportCenter);
        assertEquals(VALID_SPORTCENTER_CAPACITY, sportCenter.getMaxCapacity());
    }

    @Test
    public void testUpdateSportCenterMaxCapacityInvalid() {
        // Setup
        HttpEntity<Integer> request = new HttpEntity<>(INVALID_SPORTCENTER_CAPACITY);

        // Act
        ResponseEntity<ErrorDto> response = client.exchange("/sportcenter/capacity", HttpMethod.PUT, request,
                ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertTrue(error.getErrors().contains("Max capacity cannot be negative"));
    }

    @Test
    public void testUpdateSportCenterHours() {
        // Setup
        HttpEntity<HoursDto> request = new HttpEntity<>(new HoursDto(VALID_SPORTCENTER_OPENING_TIME,
                VALID_SPORTCENTER_CLOSING_TIME));

        // Act
        ResponseEntity<SportCenterDto> response = client.exchange("/sportcenter/hours", HttpMethod.PUT, request,
                SportCenterDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SportCenterDto sportCenter = response.getBody();
        assertNotNull(sportCenter);
        assertEquals(VALID_SPORTCENTER_OPENING_TIME, Time.valueOf(sportCenter.getOpeningTime()));
        assertEquals(VALID_SPORTCENTER_CLOSING_TIME, Time.valueOf(sportCenter.getClosingTime()));
    }

    @Test
    public void testUpdateSportCenterHoursInvalid() {
        // Setup
        HttpEntity<HoursDto> request = new HttpEntity<>(new HoursDto(INVALID_SPORTCENTER_OPENING_TIME,
                INVALID_SPORTCENTER_CLOSING_TIME));

        // Act
        ResponseEntity<ErrorDto> response = client.exchange("/sportcenter/hours", HttpMethod.PUT, request,
                ErrorDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto error = response.getBody();
        assertNotNull(error);
        assertTrue(error.getErrors().contains("Opening time cannot be after closing time"));
    }
}
