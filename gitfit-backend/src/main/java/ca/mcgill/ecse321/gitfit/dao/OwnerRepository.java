package ca.mcgill.ecse321.gitfit.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.gitfit.model.Owner;

@Repository
public interface OwnerRepository extends CrudRepository<Owner, Integer> {
    Owner findOwnerByUsername(String username);
}
