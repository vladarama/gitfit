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

import java.sql.Time;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class SportCenterServiceTests {

    @Mock
    private SportCenterRepository sportCenterRepository;

    @InjectMocks
    private SportCenterService sportCenterService;

    private static final int CENTER_ID = 1360;
    private static final String CENTER_NAME = "McGill Gym";
    private static final int CENTER_MAX_CAPACITY = 100;
    private static final Time CENTER_OPEN_TIME = Time.valueOf("08:00:00");
    private static final Time CENTER_CLOSE_TIME = Time.valueOf("22:00:00");

    private static final String CENTER_NAME_NEW = "UWU-Town";
    private static final int CENTER_MAX_CAPACITY_NEW = 200;
    private static final Time CENTER_OPEN_TIME_NEW = Time.valueOf("07:00:00");
    private static final Time CENTER_CLOSE_TIME_NEW = Time.valueOf("23:00:00");

    @BeforeEach
    public void setMockOutput() {
        lenient().when(sportCenterRepository.findAll()).thenReturn(Arrays.asList(
                createSportCenter(CENTER_ID, CENTER_NAME, CENTER_MAX_CAPACITY, CENTER_OPEN_TIME, CENTER_CLOSE_TIME)));
        lenient().when(sportCenterRepository.save(any(SportCenter.class))).thenAnswer((InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        });
    }

    @Test
    public void testGetSportCenter() {
        // Act
        SportCenter readSportCenter = sportCenterService.getSportCenter();
        System.out.println(readSportCenter.toString());

        // Assert
        assertNotNull(readSportCenter);
        assertEquals(CENTER_ID, readSportCenter.getId());
        assertEquals(CENTER_NAME, readSportCenter.getName());
        assertEquals(CENTER_MAX_CAPACITY, readSportCenter.getMaxCapacity());
        assertEquals(CENTER_OPEN_TIME, readSportCenter.getOpeningTime());
        assertEquals(CENTER_CLOSE_TIME, readSportCenter.getClosingTime());
        verify(sportCenterRepository, times(1)).findAll();
    }

    @Test
    public void testSetSportCenterName() {
        SportCenter sportCenter = null;
        try {
            sportCenter = sportCenterService.setSportCenterName(CENTER_NAME_NEW);
        } catch (SportCenterException e) {
            fail();
        }
        assertNotNull(sportCenter);
        assertEquals(CENTER_NAME_NEW, sportCenter.getName());
        verify(sportCenterRepository, times(1)).save(any(SportCenter.class));
    }

    @Test
    public void testSetSportCenterNameNull() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sportCenterService.setSportCenterName(null);
        }, "Name cannot be null or empty");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sportCenterRepository, never()).save(any(SportCenter.class));
    }

    @Test
    public void testSetSportCenterNameEmpty() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sportCenterService.setSportCenterName("");
        }, "Name cannot be null or empty");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sportCenterRepository, never()).save(any(SportCenter.class));
    }

    @Test
    public void testSetSportCenterNameSpaces() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sportCenterService.setSportCenterName("     ");
        }, "Name cannot be null or empty");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sportCenterRepository, never()).save(any(SportCenter.class));
    }

    @Test
    public void testSetSportCenterMaxCapacity() {
        SportCenter sportCenter = null;
        try {
            sportCenter = sportCenterService.setSportCenterMaxCapacity(CENTER_MAX_CAPACITY_NEW);
        } catch (SportCenterException e) {
            fail();
        }
        assertNotNull(sportCenter);
        assertEquals(CENTER_MAX_CAPACITY_NEW, sportCenter.getMaxCapacity());
        verify(sportCenterRepository, times(1)).save(any(SportCenter.class));
    }

    @Test
    public void testSetSportCenterMaxCapacityNegative() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sportCenterService.setSportCenterMaxCapacity(-1);
        }, "Max capacity cannot be negative");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sportCenterRepository, never()).save(any(SportCenter.class));
    }

    @Test
    public void testSetOpenHours() {
        SportCenter sportCenter = null;
        try {
            sportCenter = sportCenterService.setOpenHours(CENTER_OPEN_TIME_NEW, CENTER_CLOSE_TIME_NEW);
        } catch (SportCenterException e) {
            fail();
        }
        assertNotNull(sportCenter);
        assertEquals(CENTER_OPEN_TIME_NEW, sportCenter.getOpeningTime());
        assertEquals(CENTER_CLOSE_TIME_NEW, sportCenter.getClosingTime());
        verify(sportCenterRepository, times(1)).save(any(SportCenter.class));
    }

    @Test
    public void testSetOpenHoursNull() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sportCenterService.setOpenHours(null, null);
        }, "Opening and closing time cannot be null");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sportCenterRepository, never()).save(any(SportCenter.class));
    }

    @Test
    public void testSetOpenHoursClosingBeforeOpening() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sportCenterService.setOpenHours(Time.valueOf("23:00:00"), Time.valueOf("07:00:00"));
        }, "Opening and closing time cannot be null");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sportCenterRepository, never()).save(any(SportCenter.class));
    }

    @Test
    public void testSetOpenHoursClosingEqualsOpening() {
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            sportCenterService.setOpenHours(Time.valueOf("07:00:00"), Time.valueOf("07:00:00"));
        }, "Opening time cannot be same as closing time");
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verify(sportCenterRepository, never()).save(any(SportCenter.class));
    }

    private static SportCenter createSportCenter(int id, String name, int maxCapacity, Time openingTime,
            Time closingTime) {
        SportCenter sportCenter = new SportCenter();
        sportCenter.setId(id);
        sportCenter.setName(name);
        sportCenter.setMaxCapacity(maxCapacity);
        sportCenter.setOpeningTime(openingTime);
        sportCenter.setClosingTime(closingTime);
        return sportCenter;
    }
}