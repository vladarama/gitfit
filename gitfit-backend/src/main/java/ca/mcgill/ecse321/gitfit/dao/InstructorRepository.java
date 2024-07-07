package ca.mcgill.ecse321.gitfit.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.gitfit.model.Instructor;

@Repository
public interface InstructorRepository extends CrudRepository<Instructor, Integer> {
    Instructor findInstructorByUsername(String username);
}
