package ca.mcgill.ecse321.gitfit.dao;

import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SessionRepositoryTests {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private FitnessClassRepository fitnessClassRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        sessionRepository.deleteAll();
        fitnessClassRepository.deleteAll();
        instructorRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    public void testSessionPersistence() {

        SportCenter sportCenter = new SportCenter();
        sportCenter = sportCenterRepository.save(sportCenter);

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setSportCenter(sportCenter);
        fitnessClass = fitnessClassRepository.save(fitnessClass);

        Instructor instructor = new Instructor();
        instructor.setUsername("Jimmy Jim");
        instructor.setSportCenter(sportCenter);
        instructor = instructorRepository.save(instructor);

        int aPrice = 69;
        Time aEndTime = Time.valueOf("12:00:00");
        Time aStartTime = Time.valueOf("11:00:00");
        Date aDate = Date.valueOf("2020-11-11");

        Session session = new Session();
        session.setPrice(aPrice);
        session.setEndTime(aEndTime);
        session.setStartTime(aStartTime);
        session.setDate(aDate);
        session.setFitnessClass(fitnessClass);
        session.setInstructor(instructor);
        session.setSportCenter(sportCenter);
        session = sessionRepository.save(session);

        // getId from saved object
        int sessionId = session.getId();
        int fitnessClassId = fitnessClass.getId();
        int instructorId = instructor.getId();
        int sportCenterId = sportCenter.getId();

        // read back object from database
        Session sessionFromDB = sessionRepository.findSessionById(sessionId);

        // assertions
        assertEquals(sessionId, sessionFromDB.getId());
        assertEquals(aPrice, sessionFromDB.getPrice());
        assertEquals(aEndTime, sessionFromDB.getEndTime());
        assertEquals(aStartTime, sessionFromDB.getStartTime());
        assertEquals(aDate, sessionFromDB.getDate());
        assertEquals(fitnessClassId, sessionFromDB.getFitnessClass().getId());
        assertEquals(instructorId, sessionFromDB.getInstructor().getId());
        assertEquals(sportCenterId, sessionFromDB.getSportCenter().getId());
    }
}
