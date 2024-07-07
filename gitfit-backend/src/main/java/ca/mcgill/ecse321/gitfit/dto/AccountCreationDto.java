
package ca.mcgill.ecse321.gitfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AccountCreationDto {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Pattern(regexp = ".+@.+\\.com$", message = "Email must end with @ (domain) .com")
    private String email;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String LastName;

    public AccountCreationDto(String username, String email, String firstName, String LastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.LastName = LastName;
    }
}