package ca.mcgill.ecse321.gitfit.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gitfit.dto.SessionDto;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.service.FitnessClassService;
import ca.mcgill.ecse321.gitfit.service.InstructorAccountService;
import ca.mcgill.ecse321.gitfit.service.SessionService;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class SessionRestController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private InstructorAccountService instructorService;
    @Autowired
    private FitnessClassService fitnessClassService;

    /**
     * Create a session
     * 
     * @author William Wang (wangwiza)
     * @param sessionDto
     * @return
     */
    @PostMapping(value = { "/sessions", "/sessions/" })
    public SessionDto createSession(@RequestBody SessionDto sessionDto) {
        Instructor instructor = instructorService.getInstructor(sessionDto.getInstructorUsername());
        FitnessClass fitnessClass = fitnessClassService.getFitnessClassByName(sessionDto.getFitnessClassName());
        Session session = sessionService.createSession(instructor, fitnessClass, sessionDto.getPrice(),
                Time.valueOf(sessionDto.getStartTime()),
                Time.valueOf(sessionDto.getEndTime()), Date.valueOf(sessionDto.getDate()));
        return convertToDto(session);
    }

    /**
     * Get all sessions
     * 
     * @author William Wang (wangwiza)
     * @return
     */
    @GetMapping(value = { "/sessions", "/sessions/" })
    public List<SessionDto> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        List<SessionDto> sessionDtos = new ArrayList<SessionDto>();
        for (Session session : sessions) {
            sessionDtos.add(convertToDto(session));
        }
        return sessionDtos;
    }

    /**
     * Get a session by ID
     * 
     * @author William Wang (wangwiza)
     * @param id
     * @return
     */
    @GetMapping(value = { "/sessions/{id}", "/sessions/{id}/" })
    public SessionDto getSessionById(@PathVariable("id") int id) {
        Session session = sessionService.getSessionById(id);
        return convertToDto(session);
    }

    @GetMapping(value = { "/sessions/filter", "/sessions/filter/" })
    public List<SessionDto> getFilteredSessions(@RequestParam(required = false) String instructorUsername,
            @RequestParam(required = false) String fitnessClassName, @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime) {
        java.sql.Date sqlStartDate = null;
        java.sql.Date sqlEndDate = null;
        java.sql.Time sqlStartTime = null;
        java.sql.Time sqlEndTime = null;

        if (startDate != null) {
            sqlStartDate = Date.valueOf(startDate);
        }
        if (endDate != null) {
            sqlEndDate = Date.valueOf(endDate);
        }
        if (startTime != null) {
            sqlStartTime = Time.valueOf(startTime);
        }
        if (endTime != null) {
            sqlEndTime = Time.valueOf(endTime);
        }
        Instructor instructor = null;
        FitnessClass fitnessClass = null;
        if (instructorUsername != null) {
            instructor = instructorService.getInstructor(instructorUsername);
        }
        if (fitnessClassName != null) {
            fitnessClass = fitnessClassService.getFitnessClassByName(fitnessClassName);
        }
        List<Session> sessions = sessionService.getSessionsByFilters(instructor, fitnessClass, maxPrice, sqlStartDate,
                sqlEndDate, sqlStartTime, sqlEndTime);
        return sessions.stream().map(this::convertToDto).toList();
    }

    /**
     * Update a session
     * 
     * @author William Wang (wangwiza)
     * @param sessionDto
     * @return
     */
    @PutMapping(value = { "/sessions", "/sessions/" })
    public SessionDto updateSession(@RequestBody SessionDto sessionDto) {
        Session session = sessionService.getSessionById(sessionDto.getId());
        session = sessionService.updateSession(session, sessionDto.getPrice(),
                Time.valueOf(sessionDto.getStartTime()),
                Time.valueOf(sessionDto.getEndTime()), Date.valueOf(sessionDto.getDate()));
        return convertToDto(session);
    }

    /**
     * Delete a session
     * 
     * @author William Wang (wangwiza)
     * @param id
     */
    @DeleteMapping(value = { "/sessions/{id}", "/sessions/{id}/" })
    public void deleteSession(@PathVariable("id") int id) {
        Session session = sessionService.getSessionById(id);
        sessionService.deleteSession(session);
    }

    /**
     * Convert a session to a DTO
     * 
     * @author William Wang (wangwiza)
     * @param session
     * @return
     */
    private SessionDto convertToDto(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("There is no such session!");
        }
        SessionDto sessionDto = new SessionDto(session.getId(), session.getPrice(),
                session.getStartTime().toLocalTime(),
                session.getEndTime().toLocalTime(), session.getDate().toLocalDate(),
                session.getInstructor().getUsername(),
                session.getFitnessClass().getName());
        return sessionDto;
    }
}
