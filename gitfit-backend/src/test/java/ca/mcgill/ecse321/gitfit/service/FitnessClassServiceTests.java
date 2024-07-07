package ca.mcgill.ecse321.gitfit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.gitfit.dao.FitnessClassRepository;
import ca.mcgill.ecse321.gitfit.exception.SportCenterException;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;
import ca.mcgill.ecse321.gitfit.model.FitnessClassApprovalStatus;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

@SpringBootTest
public class FitnessClassServiceTests {
    @Mock
    private FitnessClassRepository fitnessClassRepository;
    @Mock
    private SportCenterService sportCenterService;
    @InjectMocks
    private FitnessClassService fitnessClassService;

    @Test
    public void createValidFitnessClassTest() {
        SportCenter sportCenter = new SportCenter();

        String name = "TestFitnessClass";
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);
        when(sportCenterService.getSportCenter()).thenReturn(sportCenter);
        FitnessClass createdFitnessClass = fitnessClassService.createFitnessClass(name, description);

        assertNotNull(createdFitnessClass);
        assertEquals(name, createdFitnessClass.getName());
        assertEquals(description, createdFitnessClass.getDescription());
        verify(fitnessClassRepository, times(1)).save(any(FitnessClass.class));

    }

    @Test
    public void createFitnessClassInvalidNameOrDescriptionTest() {
        SportCenter sportCenter = new SportCenter();

        String name = null;
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);
        when(sportCenterService.getSportCenter()).thenReturn(sportCenter);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.createFitnessClass(name, description);
        });

        assertEquals("Must provide a name and a description.", exception.getMessage());
    }

    @Test
    public void createFitnessClassAlreadyExistsTest() {
        SportCenter sportCenter = new SportCenter();

        String name = "TestFitnessClass";
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);
        when(sportCenterService.getSportCenter()).thenReturn(sportCenter);
        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.createFitnessClass(name, description);
        });

        assertEquals("There is already a fitness class called " + name + ".", exception.getMessage());
    }

    @Test
    public void findFitnessClassByNameTest() {
        SportCenter sportCenter = new SportCenter();

        String name = "TestFitnessClass";
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);

        FitnessClass foundFitnessClass = fitnessClassService.getFitnessClassByName(name);

        assertNotNull(foundFitnessClass);
        assertEquals(name, foundFitnessClass.getName());
        assertEquals(description, foundFitnessClass.getDescription());
    }

    @Test
    public void findFitnessClassByNameNotFoundTest() {
        String name = "TestFitnessClass";
        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(null);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.getFitnessClassByName(name);
        });

        assertEquals("Fitness class not found.", exception.getMessage());
    }

    @Test
    public void updateApprovalStatusTest() {
        SportCenter sportCenter = new SportCenter();

        String name = "TestFitnessClass";
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);
        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);

        String status = "APPROVED";
        FitnessClass updatedFitnessClass = fitnessClassService.updateApprovalStatus(name, status);

        assertNotNull(updatedFitnessClass);
        assertEquals(name, updatedFitnessClass.getName());
        assertEquals(description, updatedFitnessClass.getDescription());
        assertEquals(FitnessClassApprovalStatus.valueOf(status), updatedFitnessClass.getApprovalStatus());
    }

    @Test
    public void updateApprovalStatusInvalidStatusTest() {
        SportCenter sportCenter = new SportCenter();

        String name = "TestFitnessClass";
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);
        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);

        String status = "INVALID";
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.updateApprovalStatus(name, status);
        });

        assertEquals("Invalid status.", exception.getMessage());
    }

    @Test
    public void updateApprovalStatusInvalidNameOrStatusTest() {
        SportCenter sportCenter = new SportCenter();

        String name = null;
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);
        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);

        String status = "APPROVED";
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.updateApprovalStatus(name, status);
        });

        assertEquals("Must provide a name and a status.", exception.getMessage());
    }

    @Test
    public void updateFitnessClassTest() {
        SportCenter sportCenter = new SportCenter();

        String name = "TestFitnessClass";
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);
        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);

        String newDescription = "NewDescription";
        FitnessClass updatedFitnessClass = fitnessClassService.updateFitnessClass(name, newDescription);

        assertNotNull(updatedFitnessClass);
        assertEquals(name, updatedFitnessClass.getName());
        assertEquals(newDescription, updatedFitnessClass.getDescription());
    }

    @Test
    public void updateFitnessClassInvalidNameOrDescriptionTest() {
        SportCenter sportCenter = new SportCenter();

        String name = null;
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);
        fitnessClass.setSportCenter(sportCenter);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);
        when(fitnessClassRepository.save(any(FitnessClass.class))).thenReturn(fitnessClass);

        String newDescription = "NewDescription";
        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.updateFitnessClass(name, newDescription);
        });

        assertEquals("Must provide a name and a description.", exception.getMessage());
    }

    @Test
    public void updateFitnessClassNotFoundTest() {
        String name = "TestFitnessClass";
        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(null);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.updateFitnessClass(name, "NewDescription");
        });

        assertEquals("Fitness class not found.", exception.getMessage());
    }

    @Test
    public void deleteRejectedFitnessClassesTest() {
        String name1 = "TestFitnessClass1";
        String description1 = "TestDescription1";
        String name2 = "TestFitnessClass2";
        String description2 = "TestDescription2";

        FitnessClass fitnessClass1 = new FitnessClass();
        fitnessClass1.setName(name1);
        fitnessClass1.setDescription(description1);
        fitnessClass1.setApprovalStatus(FitnessClassApprovalStatus.REJECTED);
        FitnessClass fitnessClass2 = new FitnessClass();
        fitnessClass2.setName(name2);
        fitnessClass2.setDescription(description2);
        fitnessClass2.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);

        List<FitnessClass> fitnessClasses = new ArrayList<>();
        fitnessClasses.add(fitnessClass1);
        fitnessClasses.add(fitnessClass2);

        when(fitnessClassRepository.findAll()).thenReturn(fitnessClasses);

        fitnessClassService.deleteRejectedFitnessClasses();

        verify(fitnessClassRepository, times(1)).delete(fitnessClass1);
        verify(fitnessClassRepository, never()).delete(fitnessClass2);

    }

    @Test
    public void deleteFitnessClassTest() {
        String name = "TestFitnessClass";
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);

        fitnessClassService.deleteFitnessClass(name);

        verify(fitnessClassRepository, times(1)).delete(fitnessClass);
    }

    @Test
    public void deleteFitnessClassInvalidNameTest() {
        String name = null;
        String description = "TestDescription";

        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName(name);
        fitnessClass.setDescription(description);

        when(fitnessClassRepository.findFitnessClassByName(name)).thenReturn(fitnessClass);

        SportCenterException exception = assertThrows(SportCenterException.class, () -> {
            fitnessClassService.deleteFitnessClass(name);
        });

        assertEquals("Must provide a name.", exception.getMessage());
    }

    @Test
    public void findAllFitnessClassesTest() {
        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName("TestFitnessClass");
        fitnessClass.setDescription("TestDescription");
        FitnessClass fitnessClass2 = new FitnessClass();
        fitnessClass2.setName("TestFitnessClass2");
        fitnessClass2.setDescription("TestDescription2");

        List<FitnessClass> fitnessClasses = new ArrayList<>();
        fitnessClasses.add(fitnessClass);
        fitnessClasses.add(fitnessClass2);

        when(fitnessClassRepository.findAll()).thenReturn(fitnessClasses);

        assertEquals(2, fitnessClassService.getAllFitnessClasses().size());
    }

    @Test
    public void findApprovedClassesTest() {
        FitnessClass fitnessClass = new FitnessClass();
        fitnessClass.setName("TestFitnessClass");
        fitnessClass.setDescription("TestDescription");
        fitnessClass.setApprovalStatus(FitnessClassApprovalStatus.APPROVED);
        FitnessClass fitnessClass2 = new FitnessClass();
        fitnessClass2.setName("TestFitnessClass2");
        fitnessClass2.setDescription("TestDescription2");
        fitnessClass2.setApprovalStatus(FitnessClassApprovalStatus.PENDING);

        List<FitnessClass> fitnessClasses = new ArrayList<>();
        fitnessClasses.add(fitnessClass);
        fitnessClasses.add(fitnessClass2);

        when(fitnessClassRepository.findAll()).thenReturn(fitnessClasses);

        List<FitnessClass> approvedClasses = fitnessClassService.getApprovedClasses();

        assertEquals(1, approvedClasses.size());
        assertEquals("TestFitnessClass", approvedClasses.get(0).getName());
        assertEquals("TestDescription", approvedClasses.get(0).getDescription());
    }

}
