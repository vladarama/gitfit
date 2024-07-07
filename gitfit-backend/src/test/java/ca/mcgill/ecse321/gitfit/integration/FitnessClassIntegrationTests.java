package ca.mcgill.ecse321.gitfit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.gitfit.dao.FitnessClassRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.dto.ErrorDto;
import ca.mcgill.ecse321.gitfit.dto.FitnessClassDto;
import ca.mcgill.ecse321.gitfit.dto.FitnessClassStatusDto;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.FitnessClassApprovalStatus;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FitnessClassIntegrationTests {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private FitnessClassRepository fitnessClassRepository;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    private final String VALID_NAME = "Yoga";
    private final String VALID_DESCRIPTION = "Yoga class";
    private final String VALID_NAME_2 = "Pilates";
    private final String VALID_DESCRIPTION_2 = "Pilates class";
    private final String INVALID_NAME = "";
    private final String INVALID_DESCRIPTION = "";
    private static final SportCenter SPORT_CENTER = new SportCenter();

    @BeforeAll
    public void setup() {
        fitnessClassRepository.deleteAll();
        sportCenterRepository.deleteAll();
        sportCenterRepository.save(SPORT_CENTER);
        fitnessClassRepository.save(new FitnessClass(VALID_NAME_2, VALID_DESCRIPTION_2, SPORT_CENTER));
    }

    @AfterAll
    public void clearDatabase() {
        fitnessClassRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateFitnessClassInvalidNameOrDescription() {
        FitnessClassDto fitnessClassDto = new FitnessClassDto(INVALID_NAME, INVALID_DESCRIPTION, FitnessClassApprovalStatus.PENDING);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(fitnessClassDto, headers);
        ResponseEntity<ErrorDto> response = client.exchange("/fitnessclasses", HttpMethod.POST, entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("Must provide a name and a description.", responseDto.getErrors().get(0));
    }

    @Test
    @Order(2)
    public void testCreateValidFitnessClass() {
        FitnessClassDto fitnessClassDto = new FitnessClassDto(VALID_NAME, VALID_DESCRIPTION, FitnessClassApprovalStatus.PENDING);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(fitnessClassDto, headers);
        ResponseEntity<FitnessClassDto> response = client.exchange("/fitnessclasses", HttpMethod.POST, entity, FitnessClassDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        FitnessClassDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(VALID_NAME, responseDto.getName());
        assertEquals(VALID_DESCRIPTION, responseDto.getDescription());
        assertEquals(FitnessClassStatusDto.PENDING, responseDto.getApprovalStatus());
    }

    @Test
    @Order(3)
    public void testCreateFitnessClassAlreadyExists() {
        FitnessClassDto fitnessClassDto = new FitnessClassDto(VALID_NAME, VALID_DESCRIPTION, FitnessClassApprovalStatus.PENDING);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(fitnessClassDto, headers);
        ResponseEntity<ErrorDto> response = client.exchange("/fitnessclasses", HttpMethod.POST, entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("There is already a fitness class called " + VALID_NAME + ".", responseDto.getErrors().get(0));
    }

    @Test
    @Order(4)
    public void testFindAllFitnessClasses() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(headers);
        ResponseEntity<FitnessClassDto[]> response = client.exchange("/fitnessclasses", HttpMethod.GET, entity, FitnessClassDto[].class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FitnessClassDto[] responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(2, responseDto.length);
        assertEquals(VALID_NAME_2, responseDto[0].getName());
        assertEquals(VALID_DESCRIPTION_2, responseDto[0].getDescription());
        assertEquals(FitnessClassStatusDto.PENDING, responseDto[0].getApprovalStatus());
        assertEquals(VALID_NAME, responseDto[1].getName());
        assertEquals(VALID_DESCRIPTION, responseDto[1].getDescription());
        assertEquals(FitnessClassStatusDto.PENDING, responseDto[1].getApprovalStatus());
    }

    @Test
    @Order(5)
    public void testFindApprovedClasses() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(headers);
        ResponseEntity<FitnessClassDto[]> response = client.exchange("/fitnessclasses/approved", HttpMethod.GET, entity, FitnessClassDto[].class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FitnessClassDto[] responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(0, responseDto.length);
    }

    @Test
    @Order(6)
    public void testFindFitnessClassByName() {
        ResponseEntity<FitnessClassDto> response = client.getForEntity("/fitnessclasses/" + VALID_NAME_2, FitnessClassDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FitnessClassDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(VALID_NAME_2, responseDto.getName());
        assertEquals(VALID_DESCRIPTION_2, responseDto.getDescription());
        assertEquals(FitnessClassStatusDto.PENDING, responseDto.getApprovalStatus());
    }

    @Test
    @Order(7)
    public void testFindNonExistingFitnessClassByName() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDto> response = client.exchange("/fitnessclasses/NonExisting", HttpMethod.GET, entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("Fitness class not found.", responseDto.getErrors().get(0));
    }

    @Test
    @Order(8)
    public void testUpdateFitnessClassApprovalStatus() {
        FitnessClassStatusDto fitnessClassStatusDto = FitnessClassStatusDto.APPROVED;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassStatusDto> entity = new HttpEntity<>(fitnessClassStatusDto, headers);
        ResponseEntity<FitnessClassDto> response = client.exchange("/fitnessclasses/" + VALID_NAME + "/approval", HttpMethod.PUT, entity, FitnessClassDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FitnessClassDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(VALID_NAME, responseDto.getName());
        assertEquals(VALID_DESCRIPTION, responseDto.getDescription());
        assertEquals(FitnessClassStatusDto.APPROVED, responseDto.getApprovalStatus());        
    }

    @Test
    @Order(9)
    public void testUpdateFitnessClass() {
        FitnessClassDto fitnessClassDto = new FitnessClassDto(VALID_NAME, "Updated description", FitnessClassApprovalStatus.PENDING);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(fitnessClassDto, headers);
        ResponseEntity<FitnessClassDto> response = client.exchange("/fitnessclasses/" + VALID_NAME, HttpMethod.PUT, entity, FitnessClassDto.class);

        assertNotNull(response);   
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FitnessClassDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals(VALID_NAME, responseDto.getName());
        assertEquals("Updated description", responseDto.getDescription());
        assertEquals(FitnessClassStatusDto.APPROVED, responseDto.getApprovalStatus());
    }

    @Test
    @Order(10)
    public void testUpdateFitnessClassInvalidNameOrDescription() {
        FitnessClassDto fitnessClassDto = new FitnessClassDto(VALID_NAME, INVALID_DESCRIPTION, FitnessClassApprovalStatus.PENDING);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(fitnessClassDto, headers);
        ResponseEntity<ErrorDto> response = client.exchange("/fitnessclasses/" + VALID_NAME, HttpMethod.PUT, entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("Must provide a name and a description.", responseDto.getErrors().get(0));
    }

    @Test
    @Order(11)
    public void testUpdateNonExistingFitnessClass() {
        FitnessClassDto fitnessClassDto = new FitnessClassDto("NonExisting", "Updated description", FitnessClassApprovalStatus.PENDING);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(fitnessClassDto, headers);
        ResponseEntity<ErrorDto> response = client.exchange("/fitnessclasses/NonExisting", HttpMethod.PUT, entity, ErrorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDto responseDto = response.getBody();
        assertNotNull(responseDto);
        assertEquals("Fitness class not found.", responseDto.getErrors().get(0));
    }
 
    @Test
    @Order(12)
    public void testDeleteRejectedClasses() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = client.exchange("/fitnessclasses/rejected", HttpMethod.DELETE, entity, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(13)
    public void testDeleteFitnessClass() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FitnessClassDto> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = client.exchange("/fitnessclasses/" + VALID_NAME, HttpMethod.DELETE, entity, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
