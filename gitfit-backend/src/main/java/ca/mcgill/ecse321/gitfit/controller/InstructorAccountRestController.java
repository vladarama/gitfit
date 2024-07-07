package ca.mcgill.ecse321.gitfit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gitfit.dto.AccountRequestDto;
import ca.mcgill.ecse321.gitfit.dto.InstructorAccountDto;
import ca.mcgill.ecse321.gitfit.dto.PasswordRequestDto;
import ca.mcgill.ecse321.gitfit.model.Instructor;
import ca.mcgill.ecse321.gitfit.service.InstructorAccountService;

/**
 * This class is responsible for handling HTTP requests for instructor
 * operations
 * 
 * 
 * @author Jatin Patel (Jatin-Pat)
 */
@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class InstructorAccountRestController {

    @Autowired
    private InstructorAccountService instructorAccountService;

    /**
     * Retrieve an instructor by username
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param username
     * @return InstructorAccountDto
     */
    @GetMapping(value = { "/instructor/{username}" })
    public InstructorAccountDto getInstructor(@PathVariable String username) {
        Instructor instructor = instructorAccountService.getInstructor(username);
        return convertToDto(instructor);
    }

    /**
     * Retrieve all instructors
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @return List of all InstructorAccountDto
     */
    @GetMapping(value = { "/instructors/", "/instructors" })
    public List<InstructorAccountDto> getAllInstructors() {
        List<InstructorAccountDto> dtoList = new ArrayList<>();

        for (Instructor instructor : instructorAccountService.getAllInstructors()) {
            dtoList.add(convertToDto(instructor));
        }

        return dtoList;
    }

    /**
     * Update an instructor's password
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param newPassword
     * @param username
     * @return InstructorAccountDto
     */
    @PutMapping(value = { "/instructor/password/", "/instructor/password" })
    public InstructorAccountDto updateInstructorPassword(@RequestBody PasswordRequestDto passwordRequestDto) {
        Instructor instructor = instructorAccountService.getInstructor(passwordRequestDto.getUsername());
        instructor = instructorAccountService.updateInstructorPassword(passwordRequestDto.getUsername(),
                passwordRequestDto.getPassword());
        return convertToDto(instructor);
    }

    /**
     * Create an instructor
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param username
     * @param email
     * @param password
     * @param lastName
     * @param firstName
     * @return InstructorAccountDto
     */
    @PostMapping(value = { "/instructor/", "/instructor" })
    public InstructorAccountDto createInstructor(@RequestBody AccountRequestDto accountRequestDto) {
        Instructor instructor = instructorAccountService.createInstructor(accountRequestDto.getUsername(),
                accountRequestDto.getEmail(), accountRequestDto.getPassword(), accountRequestDto.getLastName(),
                accountRequestDto.getFirstName());
        return convertToDto(instructor);
    }

    /**
     * Delete an instructor
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param username
     */
    @DeleteMapping(value = { "/instructor/", "/instructor" })
    public void deleteInstructor(@RequestBody String username) {
        instructorAccountService.deleteInstructor(username);
    }

    /**
     * Convert model instance to DTO instance
     * 
     * @author Jatin Patel (Jatin-Pat)
     * @param instructor
     * @return InstructorAccountDto
     */
    private InstructorAccountDto convertToDto(Instructor instructor) {
        return new InstructorAccountDto(instructor.getUsername(), instructor.getEmail(), instructor.getFirstName(),
                instructor.getLastName(), instructor.getPassword());
    }
}