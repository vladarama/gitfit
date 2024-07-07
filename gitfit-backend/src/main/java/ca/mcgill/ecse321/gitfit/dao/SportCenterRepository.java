package ca.mcgill.ecse321.gitfit.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.gitfit.model.SportCenter;

@Repository
public interface SportCenterRepository extends CrudRepository<SportCenter, Integer> {
    SportCenter findSportCenterById(int id);
}
