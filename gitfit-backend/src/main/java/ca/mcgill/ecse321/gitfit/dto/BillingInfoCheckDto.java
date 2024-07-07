package ca.mcgill.ecse321.gitfit.dto;

import jakarta.validation.constraints.Pattern;

public class BillingInfoCheckDto {

    private String country;

    private String state;

    @Pattern(regexp = "^[A-Za-z0-9 ]{5,10}$", message = "Postal code must be between 5 and 10 alphanumeric characters")
    private String postalCode;

    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    private String cardNumber;

    private String address;

    public BillingInfoCheckDto(String country, String state, String postalCode, String cardNumber, String address) {
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.address = address;
    }
}