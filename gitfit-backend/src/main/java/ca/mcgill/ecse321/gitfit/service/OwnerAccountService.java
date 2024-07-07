package ca.mcgill.ecse321.gitfit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.gitfit.dao.OwnerRepository;
import ca.mcgill.ecse321.gitfit.dto.PasswordCheckDto;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.Owner;

/**
 * This class is responsible for handling owner account operations
 * 
 * @author Jatin Patel (Jatin-Pat)
 */
@Service
public class OwnerAccountService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ValidatorService validatorService;

    /**
     * Retrieve the owner
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @return Owner
     * @throws SportCenterException if no owner found
     */
    @Transactional
    public Owner getOwner() {
        List<Owner> list = toList(ownerRepository.findAll());
        if (list.isEmpty()) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "No current owner.");
        } else {
            return list.get(0);
        }
    }

    /**
     * Update the owner's password
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param newPassword
     * @return Owner
     * @throws SportCenterException if password is invalid
     */
    @Transactional
    public Owner updateOwnerPassword(String newPassword) {
        Owner owner = getOwner();

        validatorService.validate(new PasswordCheckDto(newPassword));

        owner.setPassword(newPassword);
        ownerRepository.save(owner);
        return owner;
    }

    /**
     * Converts an iterable to a list
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param iterable
     * @return List containing all the elements from the given iterable
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}