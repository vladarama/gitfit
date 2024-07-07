package ca.mcgill.ecse321.gitfit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.gitfit.dao.FitnessClassRepository;
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.FitnessClassApprovalStatus;
import ca.mcgill.ecse321.gitfit.model.Session;
import jakarta.transaction.Transactional;

@Service
public class FitnessClassService {
    @Autowired
    private FitnessClassRepository fitnessClassRepository;
    @Autowired
    private SportCenterService sportCenterService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Transactional
    public List<FitnessClass> getAllFitnessClasses() {
        return toList(fitnessClassRepository.findAll());
    }

    @Transactional
    public List<FitnessClass> getApprovedClasses() {
        List<FitnessClass> approvedClasses = new ArrayList<FitnessClass>();
        for (FitnessClass fitnessClass : fitnessClassRepository.findAll()) {
            if (fitnessClass.getApprovalStatus() == FitnessClassApprovalStatus.APPROVED) {
                approvedClasses.add(fitnessClass);
            }
        }
        return approvedClasses;
    }

    @Transactional
    public List<FitnessClass> getPendingClasses() {
        List<FitnessClass> pendingClasses = new ArrayList<FitnessClass>();
        for (FitnessClass fitnessClass : fitnessClassRepository.findAll()) {
            if (fitnessClass.getApprovalStatus() == FitnessClassApprovalStatus.PENDING) {
                pendingClasses.add(fitnessClass);
            }
        }
        return pendingClasses;
    }

    @Transactional
    public List<FitnessClass> getRejectedClasses() {
        List<FitnessClass> rejectedClasses = new ArrayList<FitnessClass>();
        for (FitnessClass fitnessClass : fitnessClassRepository.findAll()) {
            if (fitnessClass.getApprovalStatus() == FitnessClassApprovalStatus.REJECTED) {
                rejectedClasses.add(fitnessClass);
            }
        }
        return rejectedClasses;
    }

    @Transactional
    public FitnessClass getFitnessClassByName(String name) {
        FitnessClass fitnessClass = fitnessClassRepository.findFitnessClassByName(name);
        if (fitnessClass == null) {
            throw new SportCenterException(HttpStatus.NOT_FOUND, "Fitness class not found.");

        }
        return fitnessClass;
    }

    @Transactional
    public FitnessClass createFitnessClass(String name, String description) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty()) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Must provide a name and a description.");
        }

        // if there is no existing fitness class with the given name, then create a new
        // fitness class
        try {
            getFitnessClassByName(name);
        } catch (SportCenterException e) {
            FitnessClass toCreate = new FitnessClass(name, description, sportCenterService.getSportCenter());
            return fitnessClassRepository.save(toCreate);
        }

        // otherwise, the fitness class already exists
        throw new SportCenterException(HttpStatus.BAD_REQUEST, "There is already a fitness class called " + name + ".");
    }

    @Transactional
    public FitnessClass updateApprovalStatus(String name, String status) {
        if (name == null || name.isEmpty() || status == null) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Must provide a name and a status.");
        }
        try {
            FitnessClassApprovalStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Invalid status.");
        }
        FitnessClassApprovalStatus approvalStatus = FitnessClassApprovalStatus.valueOf(status);
        FitnessClass fitnessClass = getFitnessClassByName(name);
        fitnessClass.setApprovalStatus(approvalStatus);
        return fitnessClassRepository.save(fitnessClass);
    }

    @Transactional
    public FitnessClass updateFitnessClass(String name, String description) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty()) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Must provide a name and a description.");
        }

        FitnessClass fitnessClass = getFitnessClassByName(name);
        fitnessClass.setDescription(description);
        return fitnessClassRepository.save(fitnessClass);
    }

    @Transactional
    public void deleteRejectedFitnessClasses() {
        for (FitnessClass fitnessClass : fitnessClassRepository.findAll()) {
            if (fitnessClass.getApprovalStatus() == FitnessClassApprovalStatus.REJECTED) {
                fitnessClassRepository.delete(fitnessClass);
            }
        }
    }

    @Transactional
    public void deleteFitnessClass(String name) {
        if (name == null || name.isEmpty()) {
            throw new SportCenterException(HttpStatus.BAD_REQUEST, "Must provide a name.");
        }

        FitnessClass fitnessClass = getFitnessClassByName(name);

        List<Session> sessions = sessionRepository.findByFitnessClass(fitnessClass);
        for (Session session : sessions) {
            sessionService.deleteSession(session);
        }

        fitnessClassRepository.delete(fitnessClass);
    }

    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }

}