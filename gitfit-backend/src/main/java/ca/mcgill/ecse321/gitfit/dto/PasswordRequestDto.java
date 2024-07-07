package ca.mcgill.ecse321.gitfit.dto;

public class PasswordRequestDto {
    private String username;
    private String password;

    public PasswordRequestDto(String username, String Password) {
        this.username = username;
        this.password = Password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }
}