package ca.mcgill.ecse321.gitfit.dto;

public class AccountLoginResponseDto {
    boolean success;
    String role;


    public AccountLoginResponseDto(boolean status, String role) {
            this.success = status;
            this.role = role;
        }

        public boolean getSuccess() {
            return this.success;
        }

        public String getRole() {
            return this.role;
        }

    }
