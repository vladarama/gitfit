package ca.mcgill.ecse321.gitfit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gitfit.dto.AccountLoginRequestDto;
import ca.mcgill.ecse321.gitfit.dto.AccountLoginResponseDto;
import ca.mcgill.ecse321.gitfit.service.LoginService;

/**
 * This class is responsible for handling HTTP requests for account login
 * operations
 *
 *
 *
 * @author Jatin Patel (Jatin-Pat)
 */
@CrossOrigin(origins = "http://127.0.0.1:8087")
@RestController
public class LoginRestController {

    @Autowired
    private LoginService loginService;

    /**
     * Login an account
     *
     * @author Jatin Patel (Jatin-Pat)
     * @param AccountLoginRequestDto
     * @return AccountLoginResponseDto
     */
    @PostMapping(value = { "/login/", "/login" })
    public AccountLoginResponseDto loginAccount(@RequestBody AccountLoginRequestDto loginRequest) {
        return loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

}
