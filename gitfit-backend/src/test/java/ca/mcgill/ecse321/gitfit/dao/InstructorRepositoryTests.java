package ca.mcgill.ecse321.gitfit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class InstructorRepositoryTests {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadInstructor() {

        SportCenter sportCenter = new SportCenter();
        sportCenter = sportCenterRepository.save(sportCenter);

        // create instructor properties
        String email = "InstrucorTestEmail";
        String username = "InstructorTestUsername";
        String password = "InstructortestPassword";
        String lastName = "InstructorTestLastName";
        String firstName = "InstrucorTestFirstName";

        // create instructor and set attributes
        Instructor instructor = new Instructor();
        instructor.setEmail(email);
        instructor.setUsername(username);
        instructor.setPassword(password);
        instructor.setLastName(lastName);
        instructor.setFirstName(firstName);
        instructor.setSportCenter(sportCenter);

        // save instructor to database, store persisted instructor in variable
        instructor = instructorRepository.save(instructor);

        // Read instructor from database
        // After save
        instructor = instructorRepository.findInstructorByUsername(instructor.getUsername());
        int id = sportCenter.getId();

        // Assert that instructor is not null and has correct attributes
        assertNotNull(instructor);
        assertEquals(email, instructor.getEmail());
        assertEquals(password, instructor.getPassword());
        assertEquals(lastName, instructor.getLastName());
        assertEquals(firstName, instructor.getFirstName());
        assertEquals(username, instructor.getUsername());
        assertEquals(id, instructor.getSportCenter().getId());
    }

}
