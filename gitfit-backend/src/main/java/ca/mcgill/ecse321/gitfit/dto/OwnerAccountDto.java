package ca.mcgill.ecse321.gitfit.dto;

public class OwnerAccountDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public OwnerAccountDto() {
    }

    public OwnerAccountDto(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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