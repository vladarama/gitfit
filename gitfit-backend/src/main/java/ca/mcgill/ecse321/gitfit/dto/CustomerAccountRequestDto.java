package ca.mcgill.ecse321.gitfit.dto;

public class CustomerAccountRequestDto {
    private String username;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
    private String country;
    private String state;
    private String postalCode;
    private String cardNumber;
    private String address;

    // Constructor with all fields
    public CustomerAccountRequestDto(String username, String email, String password, String lastName,
            String firstName, String country, String state, String postalCode,
            String cardNumber, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.address = address;
    }

    // Getters
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
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}