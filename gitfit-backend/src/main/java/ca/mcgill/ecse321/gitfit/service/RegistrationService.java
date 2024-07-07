package ca.mcgill.ecse321.gitfit.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.gitfit.dao.RegistrationRepository;
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.Customer;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CustomerAccountService customerService;

    // Use session service when ready
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SportCenterService sportCenterService;

    /**
     * Get a registration by id
     * 
     * @author : Vlad Arama (vladarama)
     * @param id
     * @return a registration object
     */
    @Transactional
    public Registration getRegistration(int id) {
        Registration registration = registrationRepository.findRegistrationById(id);
        if (id == 0) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Invalid input.");
        } else if (registration == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Registration not found.");
        }
        return registration;
    }

    /**
     * Get all registrations
     * 
     * @author : Vlad Arama (vladarama)
     * @return a list of registration objects
     */
    @Transactional
    public List<Registration> getAllRegistrations() {
        List<Registration> registrations = toList(registrationRepository.findAll());
        if (registrations.isEmpty()) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "No current registrations.");
        } else {
            return registrations;
        }
    }

    /**
     * Get all of a customer's registrations by username
     * 
     * @author : Vlad Arama (vladarama)
     * @param username
     * @return a list of registration objects
     */
    @Transactional
    public List<Registration> getAllCustomerRegistrations(String username) {
        Customer customer = customerService.getCustomer(username);
        List<Registration> registrations = toList(registrationRepository.findAll());
        if (username == null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Invalid input.");
        } else if (registrations.size() == 0) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Registration not found.");
        } else if (customer == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Customer not found.");
        }
        List<Registration> customerRegistrations = new ArrayList<>();
        for (Registration registration : registrations) {
            if (registration.getCustomer().getUsername().equals(customer.getUsername())) {
                customerRegistrations.add(registration);
            }
        }
        return customerRegistrations;
    }

    /**
     * Get all of a session's registrations by sessionId
     * 
     * @author : Vlad Arama (vladarama)
     * @param sessionId
     * @return a list of registration objects
     */
    @Transactional
    public List<Registration> getAllSessionRegistrations(int sessionId) {
        Session session = sessionRepository.findSessionById(sessionId);
        List<Registration> registrations = toList(registrationRepository.findAll());
        if (sessionId == 0) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Invalid input.");
        } else if (registrations.size() == 0) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Registration not found.");
        } else if (session == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Session not found.");
        }
        List<Registration> sessionRegistrations = new ArrayList<>();
        for (Registration registration : registrations) {
            if (registration.getSession().getId() == session.getId()) {
                sessionRegistrations.add(registration);
            }
        }
        return sessionRegistrations;
    }

    /**
     * Create a registration
     * 
     * @author : Vlad Arama (vladarama)
     * @param date
     * @param sessionId
     * @param username
     * @return a registration object
     */
    @Transactional
    public Registration createRegistration(Date date, int sessionId, String username) {
        Registration registration = new Registration();
        Session session = sessionRepository.findSessionById(sessionId);
        Customer customer = customerService.getCustomer(username);

        if (date == null || sessionId == 0 || username == null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Invalid input.");
        } else if (session == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Session not found.");
        } else if (customer == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        Registration existingRegistration = registrationRepository.findRegistrationBySessionAndCustomer(session,
                customer);

        if (existingRegistration != null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Registration already exists.");
        }

        registration.setDate(date);
        registration.setSession(session);
        registration.setCustomer(customer);
        registration.setSportCenter(sportCenterService.getSportCenter());
        registrationRepository.save(registration);

        return registration;
    }

    /**
     * Delete a registration
     * 
     * @author : Vlad Arama (vladarama)
     * @param id
     * @return a boolean indicating if the registration was deleted
     */
    @Transactional
    public Boolean deleteRegistration(int id) {
        Registration registration = registrationRepository.findRegistrationById(id);
        if (id == 0) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Invalid input.");
        } else if (registration == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Registration not found.");
        }
        registrationRepository.delete(registration);
        return true;
    }

    /**
     * toList helper method
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}
