package ca.mcgill.ecse321.gitfit.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.gitfit.dao.RegistrationRepository;
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SportCenterRepository sportCenterRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    /**
     * Create a session
     * 
     * @author William Wang (wangwiza)
     * @param instructor
     * @param fitnessClass
     * @param price
     * @param endTime
     * @param startTime
     * @param date
     * @return
     */
    @Transactional
    public Session createSession(Instructor instructor, FitnessClass fitnessClass, int price, Time startTime,
            Time endTime, Date date) {
        if (instructor == null || fitnessClass == null || endTime == null || startTime == null || date == null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "All fields must be filled in to create a session");
        }
        if (price < 0) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Price must be free or positive");
        }
        if (endTime.before(startTime)) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "End time must be after start time");
        }
        SportCenter sportCenter = sportCenterRepository.findAll().iterator().next();
        if (endTime.after(sportCenter.getClosingTime()) || startTime.before(sportCenter.getOpeningTime())) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Time must be within sport center hours");
        }
        List<Session> sessions = getAllSessions();
        for (Session session : sessions) {
            if (isSlotTakenByExistingSession(session, startTime, endTime, date)) {
                throw new SportCenterException(HttpStatus.BAD_REQUEST, "Time slot is already taken");
            }
        }

        Session session = new Session();
        session.setInstructor(instructor);
        session.setFitnessClass(fitnessClass);
        session.setPrice(price);
        session.setEndTime(endTime);
        session.setStartTime(startTime);
        session.setDate(date);
        session.setSportCenter(sportCenter);
        session = sessionRepository.save(session);
        return session;
    }

    /**
     * Get all sessions
     * 
     * @author William Wang (wangwiza)
     * @return
     */
    @Transactional
    public List<Session> getAllSessions() {
        return toList(sessionRepository.findAll());
    };

    /**
     * Get session by id
     * 
     * @author William Wang (wangwiza)
     * @param id
     * @return
     */
    @Transactional
    public Session getSessionById(int id) {
        Session session = sessionRepository.findSessionById(id);
        if (session == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "No session found with that ID");
        }
        return session;
    };

    /**
     * Get sessions by various filters (price, date, time, instructor, fitness
     * class)
     * 
     * @author William Wang (wangwiza)
     * @param instructorUsername
     * @param fitnessClassName
     * @param maxPrice
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return List of sessions that match the filters
     */
    @Transactional
    public List<Session> getSessionsByFilters(Instructor instructor, FitnessClass fitnessClass,
            Integer maxPrice, Date startDate, Date endDate, Time startTime,
            Time endTime) {
        if (maxPrice != null && maxPrice < 0) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Max price cannot be negative");
        }
        if (startDate != null && endDate != null && (startDate.after(endDate) || startDate.equals(endDate))) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date");
        }
        if (startTime != null && endTime != null && (startTime.after(endTime) || startTime.equals(endTime))) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Start time cannot be after end time");
        }
        List<Session> sessions = getAllSessions().stream()
                .filter(session -> instructor == null
                        || session.getInstructor().equals(instructor))
                .filter(session -> fitnessClass == null
                        || session.getFitnessClass().equals(fitnessClass))
                .filter(session -> maxPrice == null || session.getPrice() <= maxPrice)
                .filter(session -> startDate == null
                        || (session.getDate().after(startDate) || session.getDate().equals(startDate)))
                .filter(session -> endDate == null
                        || (session.getDate().before(endDate) || session.getDate().equals(endDate)))
                .filter(session -> startTime == null
                        || (session.getStartTime().after(startTime) || session.getStartTime().equals(startTime)))
                .filter(session -> endTime == null
                        || (session.getEndTime().before(endTime) || session.getEndTime().equals(endTime)))
                .collect(Collectors.toList());
        return sessions;
    }

    /**
     * Update session
     * 
     * @author William Wang (wangwiza)
     * @param newPrice
     * @param newStartTime
     * @param newEndTime
     * @param newDate
     * @return
     */
    @Transactional
    public Session updateSession(Session session, int newPrice, Time newStartTime, Time newEndTime, Date newDate) {
        if (session == null || newStartTime == null || newEndTime == null || newDate == null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "All fields must be filled in to update a session");
        }
        if (newPrice < 0) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Price must be free or positive");
        }
        if (newEndTime.before(newStartTime)) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "End time must be after start time");
        }
        SportCenter sportCenter = sportCenterRepository.findAll().iterator().next();
        if (sportCenter == null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "No sport center found");
        }
        if (newEndTime.after(sportCenter.getClosingTime()) || newStartTime.before(sportCenter.getOpeningTime())) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Time must be within sport center hours");
        }
        List<Session> sessions = getAllSessions();
        for (Session other : sessions) {
            if (isSlotTakenByExistingSession(other, newStartTime, newEndTime, newDate) && !other.equals(session)) {
                System.out.println(other);
                throw new SportCenterException(HttpStatus.BAD_REQUEST, "Time slot is already taken");
            }
        }
        session.setPrice(newPrice);
        session.setStartTime(newStartTime);
        session.setEndTime(newEndTime);
        session.setDate(newDate);
        session = sessionRepository.save(session);
        return session;
    }

    /**
     * Delete session
     * 
     * @author William Wang (wangwiza)
     * @param session
     * @return
     */
    @Transactional
    public void deleteSession(Session session) {
        if (session == null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Session must be filled in to delete");
        }
        List<Registration> registrations = registrationRepository.findBySession(session);
        for (Registration registration : registrations) {
            registrationRepository.delete(registration);
        }
        sessionRepository.delete(session);
    }

    /**
     * toList helper method (@author eventRegistration authors)
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

    private Boolean isSlotTakenByExistingSession(Session existingSession, Time startTime, Time endTime, Date date) {
        if (existingSession.getDate().equals(date)) {
            Time existingStartTime = existingSession.getStartTime();
            Time existingEndTime = existingSession.getEndTime();
            if (existingStartTime.equals(startTime) || existingEndTime.equals(endTime) ||
                    (existingStartTime.before(startTime) && existingEndTime.after(startTime)) ||
                    (existingStartTime.before(endTime) && existingEndTime.after(endTime)) ||
                    (existingStartTime.after(startTime) && existingEndTime.before(endTime))) {
                return true;
            }
        }
        return false;
    }
}
