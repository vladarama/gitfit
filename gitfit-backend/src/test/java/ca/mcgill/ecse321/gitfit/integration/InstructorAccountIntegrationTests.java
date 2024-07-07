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

import ca.mcgill.ecse321.gitfit.dao.InstructorRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.dto.AccountRequestDto;
import ca.mcgill.ecse321.gitfit.dto.InstructorAccountDto;
import ca.mcgill.ecse321.gitfit.dto.PasswordRequestDto;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InstructorAccountIntegrationTests {

        @Autowired
        private TestRestTemplate client;

        @Autowired
        private InstructorRepository instructorRepository;

        @Autowired
        private SportCenterRepository sportCenterRepository;

        private static final String USERNAME = "AnakinInstructor";
        private static final String EMAIL = "anakin@starwars.com";
        private static final String PASSWORD = "theForce123";
        private static final String LAST_NAME = "Skywalker";
        private static final String FIRST_NAME = "Anakin";
        private static final SportCenter SPORT_CENTER = new SportCenter();

        @BeforeAll
        public void setup() {
                instructorRepository.deleteAll();
                sportCenterRepository.deleteAll();
                sportCenterRepository.save(SPORT_CENTER);
                instructorRepository
                                .save(new Instructor(USERNAME, EMAIL, PASSWORD, LAST_NAME, FIRST_NAME, SPORT_CENTER));
        }

        @AfterAll
        public void clearDatabase() {
                instructorRepository.deleteAll();
                sportCenterRepository.deleteAll();
        }

        @Test
        @Order(1)
        public void testGetInstructor() {
                ResponseEntity<InstructorAccountDto> response = client.getForEntity("/instructor/" + USERNAME,
                                InstructorAccountDto.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorAccountDto instructor = response.getBody();
                assertNotNull(instructor);
                assertEquals(USERNAME, instructor.getUsername());
                assertEquals(EMAIL, instructor.getEmail());
                assertEquals(FIRST_NAME, instructor.getFirstName());
                assertEquals(LAST_NAME, instructor.getLastName());
                assertEquals(PASSWORD, instructor.getPassword());

        }

        @Test
        @Order(2)
        public void testGetInvalidInstructor() {
                String UNKNOWN_USERNAME = "000000";
                ResponseEntity<InstructorAccountDto> response = client.getForEntity("/instructor/" + UNKNOWN_USERNAME,
                                InstructorAccountDto.class);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @Order(3)
        public void testGetAllInstructors() {
                ResponseEntity<InstructorAccountDto[]> response = client.getForEntity("/instructors/",
                                InstructorAccountDto[].class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorAccountDto[] instructors = response.getBody();
                assertNotNull(instructors);
                assertEquals(1, instructors.length);
        }

        @Test
        @Order(4)
        public void testCreateValidInstructor() {
                String newUsername = "LukeInstructor";
                String newEmail = "luke@starwars.com";
                String newPassword = "LukeForce456";
                String newLastName = "Skywalker";
                String newFirstName = "Luke";

                AccountRequestDto accountRequestDto = new AccountRequestDto(newUsername, newEmail, newPassword,
                                newLastName,
                                newFirstName);

                HttpEntity<AccountRequestDto> request = new HttpEntity<>(accountRequestDto);

                ResponseEntity<InstructorAccountDto> response = client.postForEntity(
                                "/instructor/",
                                request,
                                InstructorAccountDto.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorAccountDto instructor = response.getBody();
                assertNotNull(instructor);
                assertEquals(newUsername, instructor.getUsername());
                assertEquals(newEmail, instructor.getEmail());
                assertEquals(newFirstName, instructor.getFirstName());
                assertEquals(newLastName, instructor.getLastName());
                assertEquals(newPassword, instructor.getPassword());

                ResponseEntity<InstructorAccountDto[]> response2 = client.getForEntity("/instructors/",
                                InstructorAccountDto[].class);
                InstructorAccountDto[] instructors = response2.getBody();
                assertEquals(2, instructors.length);
        }

        @Test
        @Order(5)
        public void testCreateInvalidInstructor() {
                String newUsername = "LukeInstructor";
                String newEmail = "luke@starwars.com";
                String newPassword = "badPassword";
                String newLastName = "Skywalker";
                String newFirstName = "Luke";

                AccountRequestDto accountRequestDto = new AccountRequestDto(newUsername, newEmail, newPassword,
                                newLastName,
                                newFirstName);

                HttpEntity<AccountRequestDto> request = new HttpEntity<>(accountRequestDto);

                ResponseEntity<String> response = client.postForEntity(
                                "/instructor/",
                                request,
                                String.class);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertTrue(response.getBody()
                                .contains("Password must contain at least one digit, one lowercase letter, and one uppercase letter;"));

                ResponseEntity<InstructorAccountDto[]> response2 = client.getForEntity("/instructors/",
                                InstructorAccountDto[].class);
                InstructorAccountDto[] instructors = response2.getBody();
                assertEquals(2, instructors.length);
        }

        @Test
        @Order(6)
        public void testCreateInvalidInstructor2() {
                String newUsername = "AnakinInstructor";
                String newEmail = "luke@starwars.com";
                String newPassword = "LukeForce456878";
                String newLastName = "Skywalker";
                String newFirstName = "Luke";

                AccountRequestDto accountRequestDto = new AccountRequestDto(newUsername, newEmail, newPassword,
                                newLastName,
                                newFirstName);

                HttpEntity<AccountRequestDto> request = new HttpEntity<>(accountRequestDto);

                ResponseEntity<String> response = client.postForEntity(
                                "/instructor/",
                                request,
                                String.class);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertTrue(response.getBody()
                                .contains("Username already exists."));

                ResponseEntity<InstructorAccountDto[]> response2 = client.getForEntity("/instructors/",
                                InstructorAccountDto[].class);
                InstructorAccountDto[] instructors = response2.getBody();
                assertEquals(2, instructors.length);
        }

        @Test
        @Order(7)
        public void testUpdateInstructorPassword() {
                String newPassword = "LukeForce456878";

                PasswordRequestDto accountRequestDto = new PasswordRequestDto(USERNAME, newPassword);
                HttpEntity<PasswordRequestDto> request = new HttpEntity<>(accountRequestDto);

                ResponseEntity<String> response = client.exchange(
                                "/instructor/password/",
                                HttpMethod.PUT,
                                request,
                                String.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(newPassword, instructorRepository.findInstructorByUsername(USERNAME).getPassword());
        }

        @Test
        @Order(8)
        public void testUpdateInvalidInstructorPassword() {
                String newPassword = "   ";

                PasswordRequestDto accountRequestDto = new PasswordRequestDto(USERNAME, newPassword);
                HttpEntity<PasswordRequestDto> request = new HttpEntity<>(accountRequestDto);

                ResponseEntity<String> response = client.exchange(
                                "/instructor/password/",
                                HttpMethod.PUT,
                                request,
                                String.class);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertTrue(response.getBody()
                                .contains("Password cannot be Blank;"));
        }

        @Test
        @Order(9)
        public void testDeleteInstructor() {
                HttpEntity<String> request = new HttpEntity<>(USERNAME);

                ResponseEntity<String> response = client.exchange(
                                "/instructor/",
                                HttpMethod.DELETE,
                                request,
                                String.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(1, instructorRepository.count());
        }
}