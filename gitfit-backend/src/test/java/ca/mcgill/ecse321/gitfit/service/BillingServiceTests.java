package ca.mcgill.ecse321.gitfit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.dao.BillingRepository;
import ca.mcgill.ecse321.gitfit.dao.CustomerRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.Billing;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class BillingServiceTests {

    @Mock
    private BillingRepository billingRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SportCenterRepository sportCenterRepository;

    @InjectMocks
    private BillingService billingService;

    @Test
    public void createOrUpdateBillingTest() {
        SportCenter sportCenter = new SportCenter();

        Customer customer = new Customer();
        customer.setUsername("Bob");
        customer.setSportCenter(sportCenter);

        // create billing object
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A0G4";
        String cardNumber = "8888888888888888";
        String address = "666 McGill Avenue";
        Billing billing1 = new Billing();
        billing1.setCountry(country);
        billing1.setState(state);
        billing1.setPostalCode(postalCode);
        billing1.setCardNumber(cardNumber);
        billing1.setAddress(address);

        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(customer);
        when(billingRepository.save(any(Billing.class))).thenReturn(billing1);

        // use the service
        Billing createdBilling = billingService.createOrUpdateBilling(country, state, postalCode, cardNumber, address,
                "Bob");

        // assertions
        assertNotNull(createdBilling);
        assertEquals(country, createdBilling.getCountry());
        assertEquals(state, createdBilling.getState());
        assertEquals(postalCode, createdBilling.getPostalCode());
        assertEquals(cardNumber, createdBilling.getCardNumber());
        assertEquals(address, createdBilling.getAddress());
        verify(billingRepository, times(1)).save(any(Billing.class));
    }

    @Test
    public void createOrUpdateNonExistingCustomerBillingTest() {
        SportCenter sportCenter = new SportCenter();

        Customer customer = new Customer();
        customer.setUsername("Bob");
        customer.setSportCenter(sportCenter);

        // create billing object
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A0G4";
        String cardNumber = "8888888888888888";
        String address = "666 McGill Avenue";
        Billing billing1 = new Billing();
        billing1.setCountry(country);
        billing1.setState(state);
        billing1.setPostalCode(postalCode);
        billing1.setCardNumber(cardNumber);
        billing1.setAddress(address);

        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(null);
        when(billingRepository.save(any(Billing.class))).thenReturn(billing1);

        // use the service
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            billingService.createOrUpdateBilling(country, state, postalCode, cardNumber, address, "Bob");
        });

        assertEquals("The customer does not exist.", exception.getMessage());
    }

    @Test
    public void createOrUpdateIncompleteFieldBillingTest() {
        Customer customer = new Customer();
        customer.setUsername("Bob");

        // create billing object
        String country = "Canada";
        String state = "";
        String postalCode = "H3A0G4";
        String cardNumber = "8888888888888888";
        String address = "666 McGill Avenue";
        Billing billing1 = new Billing();
        billing1.setCountry(country);
        billing1.setState(state);
        billing1.setPostalCode(postalCode);
        billing1.setCardNumber(cardNumber);
        billing1.setAddress(address);

        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(customer);
        when(billingRepository.save(any(Billing.class))).thenReturn(billing1);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            billingService.createOrUpdateBilling(country, state, postalCode, cardNumber, address, "Bob");
        });

        assertEquals("The billing information fields must be completed.", exception.getMessage());

    }

    @Test
    public void createOrUpdateBillingWithInvalidPostalCodeTest() {
        Customer customer = new Customer();
        customer.setUsername("Bob");

        // create billing object
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A0";
        String cardNumber = "8888888888888888";
        String address = "666 McGill Avenue";
        Billing billing1 = new Billing();
        billing1.setCountry(country);
        billing1.setState(state);
        billing1.setPostalCode(postalCode);
        billing1.setCardNumber(cardNumber);
        billing1.setAddress(address);

        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(customer);
        when(billingRepository.save(any(Billing.class))).thenReturn(billing1);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            billingService.createOrUpdateBilling(country, state, postalCode, cardNumber, address, "Bob");
        });

        assertEquals("Postal code must be between 5 and 10 alphanumeric characters", exception.getMessage());
    }

    @Test
    public void createOrUpdateBillingWithInvalidCardNumberTest() {
        Customer customer = new Customer();
        customer.setUsername("Bob");

        // create billing object
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A0G4";
        String cardNumber = "888888888888";
        String address = "666 McGill Avenue";
        Billing billing1 = new Billing();
        billing1.setCountry(country);
        billing1.setState(state);
        billing1.setPostalCode(postalCode);
        billing1.setCardNumber(cardNumber);
        billing1.setAddress(address);

        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(customer);
        when(billingRepository.save(any(Billing.class))).thenReturn(billing1);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            billingService.createOrUpdateBilling(country, state, postalCode, cardNumber, address, "Bob");
        });

        assertEquals("Card number must be 15 or 16 digits", exception.getMessage());
    }

    @Test
    public void deleteBillingTest() {
        Customer customer = new Customer();
        customer.setUsername("Bob");

        // create billing object
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A0G4";
        String cardNumber = "8888888888888888";
        String address = "666 McGill Avenue";
        Billing billing1 = new Billing();
        billing1.setCountry(country);
        billing1.setState(state);
        billing1.setPostalCode(postalCode);
        billing1.setCardNumber(cardNumber);
        billing1.setAddress(address);
        billing1.setId(1);

        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(customer);
        when(billingRepository.findBillingByCustomer(any(Customer.class))).thenReturn(billing1);

        billingService.deleteBilling("Bob");

        verify(billingRepository, times(1)).deleteById(any(Integer.class));
    }

    @Test
    public void deleteNonExistingCustomerBillingTest() {
        Customer customer = new Customer();
        customer.setUsername("Bob");

        // create billing object
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A0G4";
        String cardNumber = "8888888888888888";
        String address = "666 McGill Avenue";
        Billing billing1 = new Billing();
        billing1.setCountry(country);
        billing1.setState(state);
        billing1.setPostalCode(postalCode);
        billing1.setCardNumber(cardNumber);
        billing1.setAddress(address);
        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(null);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            billingService.deleteBilling("Bob");
        });

        assertEquals("The customer does not exist.", exception.getMessage());
    }

    @Test
    public void deleteNonExistingBillingTest() {
        Customer customer = new Customer();
        customer.setUsername("Bob");

        // create billing object
        String country = "Canada";
        String state = "Quebec";
        String postalCode = "H3A0G4";
        String cardNumber = "8888888888888888";
        String address = "666 McGill Avenue";

        // mock behaviours
        when(customerRepository.findCustomerByUsername(any(String.class))).thenReturn(customer);
        when(billingRepository.findBillingByCustomer(any(Customer.class))).thenReturn(null);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            billingService.deleteBilling("Bob");
        });

        assertEquals("The customer does not have billing set up.", exception.getMessage());
    }

}
