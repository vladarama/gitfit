package ca.mcgill.ecse321.gitfit.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class SessionDto {
    private int id;
    private int price;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String instructorUsername;
    private String fitnessClassName;

    public SessionDto() {
    }

    public SessionDto(int id, int price, LocalTime startTime, LocalTime endTime, LocalDate date,
            String instructorUsername, String fitnessClassName) {
        this.id = id;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.instructorUsername = instructorUsername;
        this.fitnessClassName = fitnessClassName;
    }

    public SessionDto(int id, int price, Time startTime, Time endTime, Date date,
            String instructorUsername, String fitnessClassName) {
        this.id = id;
        this.price = price;
        this.startTime = startTime.toLocalTime();
        this.endTime = endTime.toLocalTime();
        this.date = date.toLocalDate();
        this.instructorUsername = instructorUsername;
        this.fitnessClassName = fitnessClassName;
    }

    public SessionDto(int price, Time startTime, Time endTime, Date date,
            String instructorUsername, String fitnessClassName) {
        this.price = price;
        this.startTime = startTime.toLocalTime();
        this.endTime = endTime.toLocalTime();
        this.date = date.toLocalDate();
        this.instructorUsername = instructorUsername;
        this.fitnessClassName = fitnessClassName;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getInstructorUsername() {
        return instructorUsername;
    }

    public String getFitnessClassName() {
        return fitnessClassName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setInstructorUsername(String instructorUsername) {
        this.instructorUsername = instructorUsername;
    }

    public void setFitnessClassName(String fitnessClassName) {
        this.fitnessClassName = fitnessClassName;
    }
}