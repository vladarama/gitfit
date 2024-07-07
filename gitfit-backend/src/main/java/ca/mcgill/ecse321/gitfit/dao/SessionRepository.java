package ca.mcgill.ecse321.gitfit.dao;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {
    Session findSessionById(int id);

    List<Session> findByFitnessClass(FitnessClass fitnessClass);

    List<Session> findByInstructor(Instructor instructor);

    List<Session> findByInstructorAndFitnessClass(Instructor instructor, FitnessClass fitnessClass);

    List<Session> findByPriceLessThanEqual(int price);

    List<Session> findByDateBetween(Date startDate, Date endDate);

    List<Session> findByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(Time minTime, Time maxTime);
}
