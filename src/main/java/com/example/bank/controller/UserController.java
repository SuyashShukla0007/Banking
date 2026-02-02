package com.example.bank.controller;

import com.example.bank.DTO.User.UserLoginRequest;
import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.services.user.UserAuthenticationService;
import com.example.bank.services.user.UserManagementService;
import com.example.bank.services.user.UserRegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserManagementService userManagementService;

    public UserController(UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService,
            UserManagementService userManagementService) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
    }

    // testing
    @GetMapping("/")
    public String test() {
        return "hello";
    }

    // create user
    @PostMapping("/create")
    public String createUser(@RequestBody UserRegisterRequest request) {
        try {
            userRegistrationService.createUser(request);
            return "Successfull";
        } catch (RuntimeException e) {
            throw new RuntimeException("user email already exists " + e);
        }
    }

    // login user
    @PostMapping("/login")
    public String userLogin(@RequestBody UserLoginRequest request) {
        try {
            userAuthenticationService.login(request.getEmail(), request.getPassword());
            return "Login successful";
        } catch (RuntimeException e) {
            throw new RuntimeException("check credentials " + e);
        }
    }

    @GetMapping("/delete-user/{id}")
    public String delete(@PathVariable Long id) {
        try {
            userManagementService.deleteUser(id);
            return "user deleted successfully";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // @GetMapping("/getDetails")

}
