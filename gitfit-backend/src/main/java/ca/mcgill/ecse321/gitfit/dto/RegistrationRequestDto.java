package ca.mcgill.ecse321.gitfit.dto;

import java.sql.Date;

public class RegistrationRequestDto {
    private Date date;
    private int sessionId;
    private String customerUsername;

    public RegistrationRequestDto() {
    }

    public RegistrationRequestDto(Date date, int sessionId, String customerUsername) {
        this.date = date;
        this.sessionId = sessionId;
        this.customerUsername = customerUsername;
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
