package ca.mcgill.ecse321.gitfit.dto;

import java.sql.Time;
import java.time.LocalTime;

public class SportCenterDto {

    private String name;
    private int maxCapacity;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public SportCenterDto(String name, int maxCapacity, Time openingTime, Time closingTime) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.openingTime = openingTime.toLocalTime();
        this.closingTime = closingTime.toLocalTime();
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}