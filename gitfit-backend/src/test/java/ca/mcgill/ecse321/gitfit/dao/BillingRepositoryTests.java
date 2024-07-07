package ca.mcgill.ecse321.gitfit.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.model.Billing;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class BillingRepositoryTests {

    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SportCenterRepository sportCenterRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        billingRepository.deleteAll();
        customerRepository.deleteAll();
        sportCenterRepository.deleteAll();
    }

    public void testBillingPersistence() {
        // create Customer object and save it to database
        SportCenter sportCenter = new SportCenter();
        sportCenter = sportCenterRepository.save(sportCenter);

        Customer customer = new Customer();
        customer.setSportCenter(sportCenter);
        customer.setUsername("Bob");
        customer = customerRepository.save(customer);

        // create Billing object and save it to database
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A 0G4";
        String cardNumber = "8888 8888 8888 8888";
        String address = "McGill Avenue";
        Billing billing = new Billing(country, state, postalCode, cardNumber,
                address, customer);
        billing = billingRepository.save(billing);

        // getId from saved billing object, customer object, sportCenter object
        int billingId = billing.getId();
        String username = customer.getUsername();
        int sportCenterId = sportCenter.getId();

        // read back billing from database
        Billing billingFromDB = billingRepository.findBillingById(billingId);

        // assertions
        assertNotNull(billing);
        assertEquals(billingId, billingFromDB.getId());
        assertEquals(country, billingFromDB.getCountry());
        assertEquals(state, billingFromDB.getState());
        assertEquals(postalCode, billingFromDB.getPostalCode());
        assertEquals(cardNumber, billingFromDB.getCardNumber());
        assertEquals(address, billingFromDB.getAddress());
        assertEquals(username, billingFromDB.getCustomer().getUsername());
        assertEquals(sportCenterId, billing.getCustomer().getSportCenter().getId());
    }

}
