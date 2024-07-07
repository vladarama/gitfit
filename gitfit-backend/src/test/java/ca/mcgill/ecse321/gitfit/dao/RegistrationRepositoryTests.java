package ca.mcgill.ecse321.gitfit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class RegistrationRepositoryTests {
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

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
        sessionRepository.deleteAll();
        customerRepository.deleteAll();
        fitnessClassRepository.deleteAll();
        instructorRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    public void testRegistrationPersistence() {
        // create sport center
        SportCenter sportCenter = new SportCenter();
        sportCenter = sportCenterRepository.save(sportCenter);

        // create customer
        Customer customer = new Customer();
        customer.setUsername("Jimmy Johnson");
        customer.setSportCenter(sportCenter);
        customer = customerRepository.save(customer);

        // create instructor
        Instructor instructor = new Instructor();
        instructor.setUsername("Jimmy Jimz");
        instructor.setSportCenter(sportCenter);
        instructor = instructorRepository.save(instructor);

        // create fitness class
        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setSportCenter(sportCenter);
        fitnessClass = fitnessClassRepository.save(fitnessClass);

        // create session
        Session session = new Session();
        session.setInstructor(instructor);
        session.setFitnessClass(fitnessClass);
        session.setSportCenter(sportCenter);
        sessionRepository.save(session);

        // create registration
        Registration registration = new Registration();
        Date aDate = Date.valueOf("2024-02-01");
        registration.setDate(aDate);
        registration.setSession(session);
        registration.setCustomer(customer);
        registration.setSportCenter(sportCenter);
        registrationRepository.save(registration);

        // getId from saved object
        int sessionId = session.getId();
        int customerId = customer.getId();
        int registartionId = registration.getId();

        // read from database
        Registration registrationFromDb = registrationRepository.findRegistrationById(registartionId);

        // check if the object from the db is the same as the object that was saved
        assertNotNull(registrationFromDb);
        assertNotNull(registrationFromDb.getId());

        assertEquals(aDate, registrationFromDb.getDate());
        assertEquals(sessionId, registrationFromDb.getSession().getId());
        assertEquals(customerId, registrationFromDb.getCustomer().getId());
    }
}
