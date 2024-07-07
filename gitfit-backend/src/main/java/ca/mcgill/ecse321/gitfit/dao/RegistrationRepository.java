package ca.mcgill.ecse321.gitfit.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Integer> {
    Registration findRegistrationById(int id);

    Registration findRegistrationBySessionAndCustomer(Session session, Customer customer);

    List<Registration> findBySession(Session session);
}
