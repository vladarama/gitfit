package ca.mcgill.ecse321.gitfit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.gitfit.dao.RegistrationRepository;
import ca.mcgill.ecse321.gitfit.dao.SessionRepository;
import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.model.Registration;
import ca.mcgill.ecse321.gitfit.model.Session;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class SessionServiceTests {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SportCenterRepository sportCenterRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private SessionService sessionService;

    private static final int SESSION_ID = 1;
    private static final int SESSION_PRICE = 10;
    private static final Time SESSION_START_TIME = Time.valueOf("10:00:00");
    private static final Time SESSION_END_TIME = Time.valueOf("11:00:00");
    private static final Date SESSION_DATE = Date.valueOf("2024-11-11");

    private static final int SESSION2_PRICE = 20;
    private static final Time SESSION2_START_TIME = Time.valueOf("12:00:00");
    private static final Time SESSION2_END_TIME = Time.valueOf("13:00:00");
    private static final Date SESSION2_DATE = Date.valueOf("2024-11-12");

    private static final int SESSION3_PRICE = 30;
    private static final Time SESSION3_START_TIME = Time.valueOf("14:00:00");
    private static final Time SESSION3_END_TIME = Time.valueOf("15:00:00");
    private static final Date SESSION3_DATE = Date.valueOf("2024-11-13");

    private static final int INVALID_SESSION_ID = 2;
    private static final int INVALID_SESSION_PRICE = -1;
    private static final Time INVALID_SESSION_START_TIME = Time.valueOf("6:00:00");
    private static final Time INVALID_SESSION_END_TIME = Time.valueOf("23:00:00");

    private static final Time SPORT_CENTER_OPENING_TIME = Time.valueOf("08:00:00");
    private static final Time SPORT_CENTER_CLOSING_TIME = Time.valueOf("22:00:00");

    private static final String INSTRUCTOR_USERNAME = "master shifu";
    private static final String INSTRUCTOR2_USERNAME = "master oogway";

    private static final String FITNESS_CLASS_NAME = "kung fu";
    private static final String FITNESS_CLASS2_NAME = "karate";

    // Sport center
    private SportCenter SPORT_CENTER = new SportCenter();

    // Sessions
    private Session SESSION = new Session();
    private Session SESSION2 = new Session();
    private Session SESSION3 = new Session();

    private Instructor INSTRUCTOR = new Instructor();
    private Instructor INSTRUCTOR2 = new Instructor();

    private FitnessClass FITNESS_CLASS = new FitnessClass();
    private FitnessClass FITNESS_CLASS2 = new FitnessClass();

    private Registration REGISTRATION = new Registration();

    @BeforeEach
    public void setMockOutput() {
        SPORT_CENTER.setOpeningTime(SPORT_CENTER_OPENING_TIME);
        SPORT_CENTER.setClosingTime(SPORT_CENTER_CLOSING_TIME);
        INSTRUCTOR.setUsername(INSTRUCTOR_USERNAME);
        INSTRUCTOR2.setUsername(INSTRUCTOR2_USERNAME);
        FITNESS_CLASS.setName(FITNESS_CLASS_NAME);
        FITNESS_CLASS2.setName(FITNESS_CLASS2_NAME);
        REGISTRATION.setSession(SESSION);

        SESSION.setId(SESSION_ID);
        SESSION.setPrice(SESSION_PRICE);
        SESSION.setStartTime(SESSION_START_TIME);
        SESSION.setEndTime(SESSION_END_TIME);
        SESSION.setDate(SESSION_DATE);
        SESSION.setInstructor(INSTRUCTOR);
        SESSION.setFitnessClass(FITNESS_CLASS);
        SESSION.setSportCenter(SPORT_CENTER);

        SESSION2.setPrice(SESSION2_PRICE);
        SESSION2.setStartTime(SESSION2_START_TIME);
        SESSION2.setEndTime(SESSION2_END_TIME);
        SESSION2.setDate(SESSION2_DATE);
        SESSION2.setInstructor(INSTRUCTOR2);
        SESSION2.setFitnessClass(FITNESS_CLASS);
        SESSION2.setSportCenter(SPORT_CENTER);

        SESSION3.setPrice(SESSION3_PRICE);
        SESSION3.setStartTime(SESSION3_START_TIME);
        SESSION3.setEndTime(SESSION3_END_TIME);
        SESSION3.setDate(SESSION3_DATE);
        SESSION3.setInstructor(INSTRUCTOR);
        SESSION3.setFitnessClass(FITNESS_CLASS2);
        SESSION3.setSportCenter(SPORT_CENTER);

        lenient().when(sessionRepository.save(any(Session.class))).thenReturn(SESSION);
        lenient().when(sessionRepository.findSessionById(SESSION_ID)).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(SESSION_ID)) {
                return SESSION;
            } else {
                return null;
            }
        });
        lenient().when(sessionRepository.findAll()).thenReturn(Arrays.asList(SESSION, SESSION2, SESSION3));
        lenient().when(sportCenterRepository.findAll()).thenReturn(Arrays.asList(SPORT_CENTER));
    }

    @Test
    public void testCreateSession() {
        lenient().when(sessionRepository.findAll()).thenReturn(Arrays.asList());
        Session session = null;
        try {
            session = sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, SESSION_START_TIME,
                    SESSION_END_TIME, SESSION_DATE);
        } catch (SportCenterException e) {
            fail();
        }
        assertNotNull(session);
        verify(sessionRepository, times(1)).save(any(Session.class));
        verify(sportCenterRepository, times(1)).findAll();
    }

    @Test
    public void testCreateSessionNullInstructor() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(null, FITNESS_CLASS, SESSION_PRICE, SESSION_START_TIME, SESSION_END_TIME,
                    SESSION_DATE);
        }, "All fields must be filled in to create a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionNullFitnessClass() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, null, SESSION_PRICE, SESSION_START_TIME, SESSION_END_TIME,
                    SESSION_DATE);
        }, "All fields must be filled in to create a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionNullStartTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, null, SESSION_END_TIME,
                    SESSION_DATE);
        }, "All fields must be filled in to create a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionNullEndTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, SESSION_START_TIME, null,
                    SESSION_DATE);
        }, "All fields must be filled in to create a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionNullDate() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, SESSION_START_TIME, SESSION_END_TIME,
                    null);
        }, "All fields must be filled in to create a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionNegativePrice() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, INVALID_SESSION_PRICE, SESSION_START_TIME,
                    SESSION_END_TIME,
                    SESSION_DATE);
        }, "Price must be free or positive");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionEndTimeBeforeStartTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, SESSION_END_TIME, SESSION_START_TIME,
                    SESSION_DATE);
        }, "End time must be after start time");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionEndTimeAfterClosingTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, SESSION_START_TIME,
                    INVALID_SESSION_END_TIME,
                    SESSION_DATE);
        }, "Time must be within sport center hours");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionStartTimeBeforeOpeningTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, INVALID_SESSION_START_TIME,
                    SESSION_END_TIME,
                    SESSION_DATE);
        }, "Time must be within sport center hours");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testCreateSessionTimeSlotTaken() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.createSession(INSTRUCTOR, FITNESS_CLASS, SESSION_PRICE, SESSION_START_TIME, SESSION_END_TIME,
                    SESSION_DATE);
        }, "Time slot is already taken");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testGetAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();

        assertEquals(3, sessions.size());
        assertEquals(SESSION, sessions.get(0));
        assertEquals(SESSION2, sessions.get(1));
        assertEquals(SESSION3, sessions.get(2));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessionById() {
        Session session = sessionService.getSessionById(SESSION_ID);

        assertNotNull(session);
        assertEquals(SESSION, session);
        verify(sessionRepository, times(1)).findSessionById(SESSION_ID);
    }

    @Test
    public void testGetSessionByIdInvalidId() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.getSessionById(INVALID_SESSION_ID);
        }, "No session found with that ID");
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(sessionRepository, times(1)).findSessionById(INVALID_SESSION_ID);
    }

    @Test
    public void testGetSessionsByFiltersInstructor() {
        List<Session> sessions = sessionService.getSessionsByFilters(INSTRUCTOR, null, null, null, null, null,
                null);
        assertEquals(2, sessions.size());
        assertEquals(SESSION, sessions.get(0));
        assertEquals(SESSION3, sessions.get(1));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessionsByFiltersFitnessClass() {
        List<Session> sessions = sessionService.getSessionsByFilters(null, FITNESS_CLASS, null, null, null, null,
                null);
        assertEquals(2, sessions.size());
        assertEquals(SESSION, sessions.get(0));
        assertEquals(SESSION2, sessions.get(1));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessionsByFiltersMaxPrice() {
        List<Session> sessions = sessionService.getSessionsByFilters(null, null, SESSION2_PRICE, null, null, null,
                null);
        assertEquals(2, sessions.size());
        assertEquals(SESSION, sessions.get(0));
        assertEquals(SESSION2, sessions.get(1));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessionsByFiltersBetweenDates() {
        List<Session> sessions = sessionService.getSessionsByFilters(null, null, null, SESSION2_DATE, SESSION3_DATE,
                null, null);
        assertEquals(2, sessions.size());
        assertEquals(SESSION2, sessions.get(0));
        assertEquals(SESSION3, sessions.get(1));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessionsByFiltersBetweenTimes() {
        List<Session> sessions = sessionService.getSessionsByFilters(null, null, null, null, null, SESSION_END_TIME,
                SESSION3_END_TIME);
        assertEquals(2, sessions.size());
        assertEquals(SESSION2, sessions.get(0));
        assertEquals(SESSION3, sessions.get(1));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessionsByFiltersAllFilters() {
        List<Session> sessions = sessionService.getSessionsByFilters(INSTRUCTOR2, FITNESS_CLASS, SESSION2_PRICE,
                SESSION2_DATE, SESSION3_DATE, SESSION2_START_TIME, SESSION2_END_TIME);
        assertEquals(1, sessions.size());
        assertEquals(SESSION2, sessions.get(0));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateSession() {
        lenient().when(sessionRepository.findAll()).thenReturn(Arrays.asList(SESSION));
        Session session = null;
        try {
            session = sessionService.updateSession(SESSION, SESSION2_PRICE, SESSION2_START_TIME, SESSION2_END_TIME,
                    SESSION2_DATE);
        } catch (SportCenterException e) {
            fail();
        }
        assertNotNull(session);
        assertEquals(SESSION_ID, session.getId());
        assertEquals(SESSION2_PRICE, session.getPrice());
        assertEquals(SESSION2_START_TIME, session.getStartTime());
        assertEquals(SESSION2_END_TIME, session.getEndTime());
        assertEquals(SESSION2_DATE, session.getDate());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    public void testUpdateSessionNullSession() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(null, SESSION2_PRICE, SESSION2_START_TIME, SESSION2_END_TIME, SESSION2_DATE);
        }, "All fields must be filled in to update a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(null);
    }

    @Test
    public void testUpdateSessionNullStartTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, SESSION2_PRICE, null, SESSION2_END_TIME, SESSION2_DATE);
        }, "All fields must be filled in to update a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testUpdateSessionNullEndTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, SESSION2_PRICE, SESSION2_START_TIME, null, SESSION2_DATE);
        }, "All fields must be filled in to update a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testUpdateSessionNullDate() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, SESSION2_PRICE, SESSION2_START_TIME, SESSION2_END_TIME, null);
        }, "All fields must be filled in to update a session");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testUpdateSessionNegativePrice() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, INVALID_SESSION_PRICE, SESSION2_START_TIME, SESSION2_END_TIME,
                    SESSION2_DATE);
        }, "Price must be free or positive");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testUpdateSessionEndTimeBeforeStartTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, SESSION2_PRICE, SESSION2_END_TIME, SESSION2_START_TIME,
                    SESSION2_DATE);
        }, "End time must be after start time");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testUpdateSessionEndTimeAfterClosingTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, SESSION2_PRICE, SESSION2_START_TIME, INVALID_SESSION_END_TIME,
                    SESSION2_DATE);
        }, "Time must be within sport center hours");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testUpdateSessionStartTimeBeforeOpeningTime() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, SESSION2_PRICE, INVALID_SESSION_START_TIME, SESSION2_END_TIME,
                    SESSION2_DATE);
        }, "Time must be within sport center hours");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testUpdateSessionTimeSlotTaken() {
        lenient().when(sessionRepository.findAll()).thenReturn(Arrays.asList(SESSION, SESSION2));
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.updateSession(SESSION, SESSION2_PRICE, SESSION2_START_TIME, SESSION2_END_TIME,
                    SESSION2_DATE);
        }, "Time slot is already taken");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).save(SESSION);
    }

    @Test
    public void testDeleteSession() {
        lenient().when(registrationRepository.findBySession(SESSION)).thenReturn(Arrays.asList(REGISTRATION));
        try {
            sessionService.deleteSession(SESSION);
        } catch (SportCenterException e) {
            fail();
        }
        verify(sessionRepository, times(1)).delete(SESSION);
        verify(registrationRepository, times(1)).findBySession(SESSION);
    }

    @Test
    public void testDeleteSessionNullSession() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sessionService.deleteSession(null);
        }, "Session must be filled in to delete");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sessionRepository, never()).delete(null);
    }
}
