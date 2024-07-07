package ca.mcgill.ecse321.gitfit.dto;

public class AccountLoginRequestDto {
    String username;
    String password;

    public AccountLoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}