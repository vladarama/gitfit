package ca.mcgill.ecse321.gitfit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gitfit.dto.AccountLoginRequestDto;
import ca.mcgill.ecse321.gitfit.dto.AccountLoginResponseDto;
import ca.mcgill.ecse321.gitfit.dto.CustomerAccountDto;
import ca.mcgill.ecse321.gitfit.dto.CustomerAccountRequestDto;
import ca.mcgill.ecse321.gitfit.dto.PasswordRequestDto;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.service.CustomerAccountService;

/**
 * This class is responsible for handling HTTP requests for customer
 * operations
 * 
 * 
 * @author Krasimir Kirov (KrasiKirov)
 */
@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class CustomerAccountRestController {

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * Retrieve a customer by username
     * 
     * @author Krasimir Kirov (KrasiKirov)
     * @param username
     * @return CustomerAccountDto
     */
    @GetMapping(value = { "/customer/{username}" })
    public CustomerAccountDto getCustomer(@PathVariable String username) {
        Customer customer = customerAccountService.getCustomer(username);
        return convertToDto(customer);
    }

    /**
     * Retrieve all customers
     * 
     * @author Krasimir Kirov (KrasiKirov)
     * @return List of all CustomerAccountDto
     */
    @GetMapping(value = { "/customers", "/customers/" })
    public List<CustomerAccountDto> getAllCustomers() {
        List<Customer> list = customerAccountService.getAllCustomers();

        List<CustomerAccountDto> dtoList = new ArrayList<>();
        for (Customer customer : list) {
            dtoList.add(convertToDto(customer));
        }
        return dtoList;
    }

    /**
     * Update a customer's password
     * 
     * @author Krasimir Kirov (KrasiKirov)
     * @param passwordRequestDto
     * @return CustomerAccountDto
     */
    @PutMapping(value = { "/customer/password", "/customer/password/" })
    public CustomerAccountDto updateCustomerPassword(@RequestBody PasswordRequestDto passwordRequestDto) {
        Customer customer = customerAccountService.getCustomer(passwordRequestDto.getUsername());
        customer = customerAccountService.updateCustomerPassword(passwordRequestDto.getUsername(),
                passwordRequestDto.getPassword());
        return convertToDto(customer);
    }

    /**
     * Create a customer
     * 
     * @author Krasimir Kirov (KrasiKirov)
     * @param customerAccountRequestDto
     * @return CustomerAccountDto
     */
    @PostMapping(value = { "/customer", "/customer/" })
    public CustomerAccountDto createCustomer(@RequestBody CustomerAccountRequestDto customerAccountRequestDto) {
        Customer customer = customerAccountService.createCustomer(customerAccountRequestDto.getUsername(),
                customerAccountRequestDto.getEmail(), customerAccountRequestDto.getPassword(),
                customerAccountRequestDto.getLastName(), customerAccountRequestDto.getFirstName(),
                customerAccountRequestDto.getCountry(), customerAccountRequestDto.getState(),
                customerAccountRequestDto.getPostalCode(), customerAccountRequestDto.getCardNumber(),
                customerAccountRequestDto.getAddress());
        return convertToDto(customer);
    }

    /**
     * Delete a customer
     * 
     * @author Krasimir Kirov (KrasiKirov)
     * @param username
     */
    @DeleteMapping(value = { "/customer", "/customer/" })
    public void deleteCustomer(@RequestBody String username) {
        customerAccountService.deleteCustomer(username);
    }

    /**
     * Convert model instance to DTO instance
     * 
     * @author Krasimir Kirov (KrasiKirov)
     * @param instructor
     * @return CustomerAccountDto
     */
    private CustomerAccountDto convertToDto(Customer customer) {
        return new CustomerAccountDto(customer.getUsername(), customer.getEmail(), customer.getFirstName(),
                customer.getLastName(),
                customer.getPassword());
    }
}