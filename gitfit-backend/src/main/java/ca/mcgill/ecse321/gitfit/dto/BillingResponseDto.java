package ca.mcgill.ecse321.gitfit.dto;

public class BillingResponseDto {
    private String country;
    private String state;
    private String postalCode;
    private String cardNumberEnd;

    private BillingResponseDto() {
    }

    public BillingResponseDto(String country, String state, String postalCode, String cardNumberEnd) {
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.cardNumberEnd = cardNumberEnd;
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

    public String getCardNumberEnd() {
        return this.cardNumberEnd;
    }
}
