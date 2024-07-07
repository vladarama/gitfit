package ca.mcgill.ecse321.gitfit.controller;

import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse321.gitfit.dto.FitnessClassStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.gitfit.service.FitnessClassService;

import ca.mcgill.ecse321.gitfit.dto.FitnessClassDto;
import ca.mcgill.ecse321.gitfit.model.FitnessClass;

@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class FitnessClassRestController {
    @Autowired
    private FitnessClassService fitnessClassService;

    @GetMapping(value = { "/fitnessclasses", "/fitnessclasses/" })
    public List<FitnessClassDto> findAllFitnessClasses() {
        return fitnessClassService.getAllFitnessClasses().stream().map(fc -> convertToDto(fc))
                .collect(Collectors.toList());
    }

    @GetMapping(value = { "/fitnessclasses/approved", "/fitnessclasses/approved/" })
    public List<FitnessClassDto> findApprovedClasses() {
        return fitnessClassService.getApprovedClasses().stream().map(fc -> convertToDto(fc))
                .collect(Collectors.toList());
    }

    @GetMapping(value = { "/fitnessclasses/pending", "/fitnessclasses/pending/" })
    public List<FitnessClassDto> findPendingClasses() {
        return fitnessClassService.getPendingClasses().stream().map(fc -> convertToDto(fc))
                .collect(Collectors.toList());
    }

    @GetMapping(value = { "/fitnessclasses/rejected", "/fitnessclasses/rejected/" })
    public List<FitnessClassDto> findRejectedClasses() {
        return fitnessClassService.getRejectedClasses().stream().map(fc -> convertToDto(fc))
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = { "/fitnessclasses/rejected", "/fitnessclasses/rejected/" })
    public void deleteRejectedClasses() {
        fitnessClassService.deleteRejectedFitnessClasses();
    }

    @GetMapping(value = { "/fitnessclasses/{name}", "/fitnessclasses/{name}/" })
    public FitnessClassDto findFitnessClassByName(@PathVariable String name) {
        FitnessClass fitnessClass = fitnessClassService.getFitnessClassByName(name);
        return convertToDto(fitnessClass);
    }

    @PostMapping(value = { "/fitnessclasses", "/fitnessclasses/" })
    @ResponseStatus(HttpStatus.CREATED)
    public FitnessClassDto createFitnessClass(@RequestBody FitnessClassDto fitnessClass) {
        FitnessClass createdFitnessClass = fitnessClassService.createFitnessClass(
                fitnessClass.getName(),
                fitnessClass.getDescription());
        return convertToDto(createdFitnessClass);
    }

    @PutMapping(value = { "/fitnessclasses/{name}/approval", "/fitnessclasses/{name}/approval/" })
    public FitnessClassDto updateApprovalStatus(@PathVariable String name, @RequestBody FitnessClassStatusDto status) {
        FitnessClass updatedFitnessClass = fitnessClassService.updateApprovalStatus(
                name,
                status.toString());
        return convertToDto(updatedFitnessClass);
    }

    @PutMapping(value = { "/fitnessclasses/{name}", "/fitnessclasses/{name}/" })
    public FitnessClassDto updateFitnessClass(@PathVariable String name, @RequestBody FitnessClassDto fitnessClass) {
        FitnessClass updatedFitnessClass = fitnessClassService.updateFitnessClass(
                name,
                fitnessClass.getDescription());
        return convertToDto(updatedFitnessClass);
    }

    @DeleteMapping(value = { "/fitnessclasses/{name}", "/fitnessclasses/{name}/" })
    public void deleteFitnessClass(@PathVariable String name) {
        fitnessClassService.deleteFitnessClass(name);
    }

    private FitnessClassDto convertToDto(FitnessClass fitnessClass) {
        return new FitnessClassDto(fitnessClass.getName(), fitnessClass.getDescription(),
                fitnessClass.getApprovalStatus());
    }
}
