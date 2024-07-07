package ca.mcgill.ecse321.gitfit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.gitfit.dao.CustomerRepository;
import ca.mcgill.ecse321.gitfit.dao.InstructorRepository;
import ca.mcgill.ecse321.gitfit.dao.OwnerRepository;
import ca.mcgill.ecse321.gitfit.dto.AccountLoginResponseDto;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Owner;

/**
 * This class is responsible for handling login operations
 *
 * @Author Jatin Patel (Jatin-Pat)
 */
@Service
public class LoginService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    /**
     * Login method
     *
     * @author Jatin Patel (Jatin-Pat)
     * @param username
     * @param password
     * @return
     */
    public AccountLoginResponseDto login(String username, String password) {
        Customer customer = customerRepository.findCustomerByUsername(username);
        if (customer != null && customer.getPassword().equals(password)) {
            return new AccountLoginResponseDto(true, "Customer");
        }
        Instructor instructor = instructorRepository.findInstructorByUsername(username);
        if (instructor != null && instructor.getPassword().equals(password)) {
            return new AccountLoginResponseDto(true, "Instructor");
        }
        Owner owner = ownerRepository.findOwnerByUsername(username);
        if (owner != null && owner.getPassword().equals(password)) {
            return new AccountLoginResponseDto(true, "Owner");
        }
        return new AccountLoginResponseDto(false, null);
    }
}
