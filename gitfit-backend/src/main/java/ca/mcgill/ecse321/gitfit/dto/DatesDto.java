package ca.mcgill.ecse321.gitfit.dto;

import java.sql.Date;
import java.time.LocalDate;

public class DatesDto {
    private LocalDate startDate;
    private LocalDate endDate;

    public DatesDto(Date startDate, Date endDate) {
        this.startDate = startDate.toLocalDate();
        this.endDate = endDate.toLocalDate();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
