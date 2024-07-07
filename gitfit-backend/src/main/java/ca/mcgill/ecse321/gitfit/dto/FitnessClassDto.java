package ca.mcgill.ecse321.gitfit.dto;

import ca.mcgill.ecse321.gitfit.model.FitnessClassApprovalStatus;

public class FitnessClassDto {
    private String name;
    private String description;
    private FitnessClassStatusDto approvalStatus;

    public FitnessClassDto() {
    }

    public FitnessClassDto(String name, String description, FitnessClassApprovalStatus status) {
        this.name = name;
        this.description = description;
        this.approvalStatus = FitnessClassStatusDto.valueOf(status.toString());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public FitnessClassStatusDto getApprovalStatus() {
        return approvalStatus;
    }
}
