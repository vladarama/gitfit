package ca.mcgill.ecse321.gitfit.controller;

import ca.mcgill.ecse321.gitfit.dto.PasswordRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gitfit.dto.OwnerAccountDto;
import ca.mcgill.ecse321.gitfit.model.Owner;
import ca.mcgill.ecse321.gitfit.service.OwnerAccountService;

/**
 * This class is responsible for handling HTTP requests for owner operations
 * 
 * @author Jatin Patel (Jatin-Pat)
 */
@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class OwnerAccountRestController {

    @Autowired
    private OwnerAccountService ownerAccountService;

    /**
     * Retrieve the owner
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @return OwnerAccountDto
     */
    @GetMapping(value = { "/owner", "/owner/" })
    public OwnerAccountDto getOwner() {
        Owner owner = ownerAccountService.getOwner();
        return convertToDto(owner);
    }

    /**
     * Update the owner's password
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param password
     * @return OwnerAccountDto
     */
    @PutMapping(value = { "/owner/password", "/owner/password/" })
    public OwnerAccountDto updateOwnerPassword(@RequestBody String password) {
        Owner owner = ownerAccountService.getOwner();
        owner = ownerAccountService.updateOwnerPassword(password);
        return convertToDto(owner);
    }

    private OwnerAccountDto convertToDto(Owner owner) {
        return new OwnerAccountDto(owner.getUsername(), owner.getFirstName(), owner.getLastName(), owner.getEmail(),
                owner.getPassword());
    }
}