package ca.mcgill.ecse321.gitfit.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
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

import ca.mcgill.ecse321.gitfit.dao.CustomerRepository;
import ca.mcgill.ecse321.gitfit.dao.FitnessClassRepository;
import ca.mcgill.ecse321.gitfit.dao.InstructorRepository;
import ca.mcgill.ecse321.gitfit.dao.RegistrationRepository;
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.dto.RegistrationRequestDto;
import ca.mcgill.ecse321.gitfit.dto.RegistrationResponseDto;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationIntegrationTests {

        @Autowired
        private TestRestTemplate client;

        @Autowired
        private RegistrationRepository registrationRepository;

        @Autowired
        private SessionRepository sessionRepository;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private FitnessClassRepository fitnessClassRepository;

        @Autowired
        private InstructorRepository instructorRepository;

        @Autowired
        private SportCenterRepository sportCenterRepository;

        private final String SPORTCENTER_NAME = "McGill Gym";
        private final int SPORTCENTER_CAPACITY = 50;
        private final Time SPORTCENTER_OPENING_TIME = Time.valueOf("07:00:00");
        private final Time SPORTCENTER_CLOSING_TIME = Time.valueOf("21:00:00");

        private Date date1 = Date.valueOf("2022-03-01");

        // Sessions
        private Session session1;
        private Session session2;

        // FitnessClasses
        private FitnessClass fitnessClass1;
        private FitnessClass fitnessClass2;

        // Instructors
        private Instructor instructor1;
        private Instructor instructor2;

        // Customers
        private Customer customer1;
        private Customer customer2;

        // SportCenter instance
        private SportCenter sportCenter;

        // Registrations
        private Registration registration1;
        private Registration registration2;

        @BeforeAll
        public void setUp() {
                registrationRepository.deleteAll();
                sessionRepository.deleteAll();
                customerRepository.deleteAll();
                fitnessClassRepository.deleteAll();
                instructorRepository.deleteAll();
                sportCenterRepository.deleteAll();

                // SportCenter instance
                sportCenter = new SportCenter();
                sportCenter.setName(SPORTCENTER_NAME);
                sportCenter.setMaxCapacity(SPORTCENTER_CAPACITY);
                sportCenter.setOpeningTime(SPORTCENTER_OPENING_TIME);
                sportCenter.setClosingTime(SPORTCENTER_CLOSING_TIME);
                sportCenterRepository.save(sportCenter);

                // FitnessClass instances
                fitnessClass1 = new FitnessClass("Yoga", "Yoga class", sportCenter);
                fitnessClass1.setSportCenter(sportCenter);
                fitnessClassRepository.save(fitnessClass1);
                fitnessClass2 = new FitnessClass("Goat", "description", sportCenter);
                fitnessClass2.setSportCenter(sportCenter);
                fitnessClassRepository.save(fitnessClass2);

                // Instructor instances
                instructor1 = new Instructor("instructor1", "email@gmail.com", "password", "lastName", "firstName",
                                sportCenter);
                instructor1.setSportCenter(sportCenter);
                instructorRepository.save(instructor1);
                instructor2 = new Instructor("instructor2", "emails@gmail.com", "password", "lastName2", "firstName2",
                                sportCenter);
                instructor2.setSportCenter(sportCenter);
                instructorRepository.save(instructor2);

                // Session instances
                session1 = new Session(SPORTCENTER_CAPACITY, SPORTCENTER_OPENING_TIME, SPORTCENTER_CLOSING_TIME, date1,
                                instructor1, fitnessClass1, sportCenter);
                sessionRepository.save(session1);
                session2 = new Session(3233, SPORTCENTER_OPENING_TIME, SPORTCENTER_CLOSING_TIME, date1,
                                instructor2, fitnessClass2, sportCenter);
                sessionRepository.save(session2);

                // Customer instances
                customer1 = new Customer("customer1", "dsds@gmail.com", "dsadas", "dsads", "dsadsa",
                                sportCenter);
                customer2 = new Customer("customer2", "dsdsz@gmail.com", "dsadas", "dsadsz", "dsadsaz",
                                sportCenter);
                customerRepository.save(customer1);
                customerRepository.save(customer2);

                // Registrations= instances
                registration1 = new Registration();
                registration1.setCustomer(customer1);
                registration1.setSession(session1);
                registration1.setSportCenter(sportCenter);
                registration1.setDate(date1);
                registrationRepository.save(registration1);

                registration2 = new Registration();
                registration2.setCustomer(customer2);
                registration2.setSession(session2);
                registration2.setSportCenter(sportCenter);
                registration2.setDate(date1);
                registrationRepository.save(registration2);
        }

        @AfterAll
        public void tearDown() {
                registrationRepository.deleteAll();
                sessionRepository.deleteAll();
                customerRepository.deleteAll();
                fitnessClassRepository.deleteAll();
                instructorRepository.deleteAll();
                sportCenterRepository.deleteAll();
        }

        @Test
        @Order(1)
        public void testGetAllRegistrations() {
                ResponseEntity<RegistrationResponseDto[]> response = client.getForEntity("/registrations",
                                RegistrationResponseDto[].class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertTrue(response.getBody().length == 2);
        }

        @Test
        @Order(2)
        public void testGetRegistrationById() {
                ResponseEntity<RegistrationResponseDto> response = client.getForEntity("/registrations/" + "2",
                                RegistrationResponseDto.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                RegistrationResponseDto reg = response.getBody();
                assertNotNull(reg);
                assertEquals(registration2.getId(), reg.getId());

                ResponseEntity<RegistrationResponseDto> response2 = client.getForEntity("/registrations/" + "1",
                                RegistrationResponseDto.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                RegistrationResponseDto reg2 = response2.getBody();
                assertNotNull(reg2);
                assertEquals(registration1.getId(), reg2.getId());
        }

        @Test
        @Order(3)
        public void testGetRegistrationByIdNotFound() {
                ResponseEntity<RegistrationResponseDto> response = client.getForEntity("/registrations/" + "5",
                                RegistrationResponseDto.class);
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @Order(4)
        public void testDeleteRegistration() {
                ResponseEntity<Void> response = client.exchange("/registrations/{id}",
                                HttpMethod.DELETE, null, Void.class,
                                registration2.getId());
                assertEquals(HttpStatus.OK, response.getStatusCode());

                // Re-fetch to confirm deletion
                ResponseEntity<RegistrationResponseDto> recheckResponse = client.getForEntity("/registrations/{id}",
                                RegistrationResponseDto.class, registration2.getId());
                assertEquals(HttpStatus.NOT_FOUND, recheckResponse.getStatusCode());

        }

        @Test
        @Order(5)
        public void testCreateRegistration() {
                RegistrationRequestDto requestDto = new RegistrationRequestDto(date1, 2, "customer2");

                HttpEntity<RegistrationRequestDto> requestEntity = new HttpEntity<>(requestDto);
                ResponseEntity<RegistrationResponseDto> response = client.postForEntity("/registrations", requestEntity,
                                RegistrationResponseDto.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody().getId());
                assertEquals(response.getBody().getSessionId(), requestDto.getSessionId());
                assertEquals(response.getBody().getCustomerUsername(), requestDto.getCustomerUsername());
        }
}
