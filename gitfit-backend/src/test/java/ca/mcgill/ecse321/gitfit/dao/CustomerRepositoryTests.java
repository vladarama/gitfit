package ca.mcgill.ecse321.gitfit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadCustomer() {

        SportCenter sportCenter = new SportCenter();
        sportCenter = sportCenterRepository.save(sportCenter);
        // Create a customer
        String username = "kirka";
        String email = "kliment.kirk@gmail.com";
        String password = "password123";
        String lastName = "Kirk";
        String firstName = "Kliment";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setLastName(lastName);
        customer.setFirstName(firstName);
        customer.setSportCenter(sportCenter);
        customer.setUsername(username);

        // Save customer
        customerRepository.save(customer);

        // Read customer from database
        customer = customerRepository.findCustomerByUsername(customer.getUsername());

        int sportCenterId = sportCenter.getId();

        // Assert that the customer is not null and has correct attributes
        assertNotNull(customer);
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertEquals(lastName, customer.getLastName());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(username, customer.getUsername());
        assertEquals(sportCenterId, customer.getSportCenter().getId());
    }
}
