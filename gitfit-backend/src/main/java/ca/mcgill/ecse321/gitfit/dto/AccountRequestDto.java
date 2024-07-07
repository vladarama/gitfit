package ca.mcgill.ecse321.gitfit.dto;

public class AccountRequestDto {
    private String username;
    private String email;
    private String password;
    private String LastName;
    private String firstName;

    public AccountRequestDto(String username, String email, String password, String LastName, String firstName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.LastName = LastName;
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return LastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
