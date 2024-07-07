package ca.mcgill.ecse321.gitfit.dto;

import java.sql.Date;

public class RegistrationResponseDto {
    private int id;
    private Date date;
    private int sessionId;
    private String customerUsername;

    public RegistrationResponseDto() {
    }

    public RegistrationResponseDto(int id, Date date, int sessionId, String customerUsername) {
        this.id = id;
        this.date = date;
        this.sessionId = sessionId;
        this.customerUsername = customerUsername;
    }

    public int getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public String getCustomerUsername() {
        return this.customerUsername;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

}
