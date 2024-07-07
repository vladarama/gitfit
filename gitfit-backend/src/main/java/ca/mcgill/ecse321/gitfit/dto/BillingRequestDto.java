package ca.mcgill.ecse321.gitfit.dto;

public class BillingRequestDto {
    private String country;
    private String state;
    private String postalCode;
    private String cardNumber;
    private String address;
    private String username;

    private BillingRequestDto() {
    }

    public BillingRequestDto(String country, String state, String postalCode, String cardNumber, String address,
            String username) {
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.address = address;
        this.username = username;
    }

    public String getCountry() {
        return this.country;
    }

    public String getState() {
        return this.state;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public String getUsername() {
        return this.username;
    }
}
