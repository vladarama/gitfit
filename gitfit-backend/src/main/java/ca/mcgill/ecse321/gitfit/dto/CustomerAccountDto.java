package ca.mcgill.ecse321.gitfit.dto;

public class CustomerAccountDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public CustomerAccountDto() {
    }

    public CustomerAccountDto(String username, String email, String firstName, String lastName, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}