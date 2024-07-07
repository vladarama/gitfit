package ca.mcgill.ecse321.gitfit.dto;

import java.sql.Time;
import java.time.LocalTime;

public class HoursDto {
    private LocalTime openingTime;
    private LocalTime closingTime;

    public HoursDto() {
    }

    public HoursDto(Time openingTime, Time closingTime) {
        this.openingTime = openingTime.toLocalTime();
        this.closingTime = closingTime.toLocalTime();
    }

    public HoursDto(LocalTime openingTime, LocalTime closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public HoursDto(String openingTime, String closingTime) {
        this.openingTime = LocalTime.parse(openingTime);
        this.closingTime = LocalTime.parse(closingTime);
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
