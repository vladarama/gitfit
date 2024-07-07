package ca.mcgill.ecse321.gitfit.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.gitfit.model.FitnessClass;

@Repository
public interface FitnessClassRepository extends CrudRepository<FitnessClass, Integer> {
    FitnessClass findFitnessClassByName(String name);
}
