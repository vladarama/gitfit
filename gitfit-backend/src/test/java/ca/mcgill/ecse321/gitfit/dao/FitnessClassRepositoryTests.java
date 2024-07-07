package ca.mcgill.ecse321.gitfit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class FitnessClassRepositoryTests {
    @Autowired
    private FitnessClassRepository fitnessClassRepository;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    @AfterEach
    public void clearDatabase() {
        fitnessClassRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadFitnessClass() {
        // Create fitness class
        String name = "Goat Yoga";
        String description = "The best yoga class";
        SportCenter sportCenter = new SportCenter();
        sportCenter = sportCenterRepository.save(sportCenter);

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        // Save fitness class
        fitnessClass = fitnessClassRepository.save(fitnessClass);

        // Read fitness class from database
        fitnessClass = fitnessClassRepository.findFitnessClassByName(fitnessClass.getName());

        // Assert fitness class is not null and has correct attributes
        assertNotNull(fitnessClass);
        assertEquals(name, fitnessClass.getName());
        assertEquals(description, fitnessClass.getDescription());
    }
}
