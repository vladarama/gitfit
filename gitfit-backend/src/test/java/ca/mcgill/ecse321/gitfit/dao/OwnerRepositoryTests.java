package ca.mcgill.ecse321.gitfit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.model.Owner;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class OwnerRepositoryTests {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadOwner() {

        // create sport center properties
        String sportCenterName = "McGill Gym";
        int maxCapacity = 100;
        Time openingTime = Time.valueOf("08:00:00");
        Time closingTime = Time.valueOf("20:00:00");

        // create owner properties
        String email = "OwnerTestEmail";
        String username = "OwnerTestUsername";
        String password = "OwnertestPassword";
        String lastName = "OwnerTestLastName";
        String firstName = "OwnerTestFirstName";

        // create sport center and set attributes
        SportCenter sportCenter = new SportCenter(sportCenterName, maxCapacity, openingTime, closingTime,
                username, email, password, lastName, firstName);

        Owner owner = sportCenter.getOwner();
        // save owner to database
        owner = ownerRepository.save(owner);

        // Read owner from database
        // After save
        owner = ownerRepository.findAll().iterator().next();
        int id = sportCenter.getId();

        // Assert that owner is not null and has correct attributes
        assertNotNull(owner);
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());
        assertEquals(lastName, owner.getLastName());
        assertEquals(firstName, owner.getFirstName());
        assertEquals(username, owner.getUsername());
        assertEquals(id, owner.getSportCenter().getId());

    }

}