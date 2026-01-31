package com.example.bank.controller;

import com.example.bank.DTO.User.UserLoginRequest;
import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.services.UserService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

// create user
    @PostMapping("/create")
    public String createUser(@RequestBody UserRegisterRequest request)
    {
        try
        {
            userService.createUser(request);
            return "Successfull";
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException("user email already exists " +e);
        }
    }

    //login user

    @PostMapping("/login")

    public String  userLogin(@RequestBody UserLoginRequest request)
    {
        try{
            userService.login(request.getEmail(), request.getPassword());
            return "Login successful";
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException("check credentials " +e);
        }

    }

    @GetMapping("/delete-user/{id}")
    public String delete(@PathVariable Long id)
    {
        try{
            userService.deleteUser(id);
            return "user deleted successfully";

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

//    @GetMapping("/getDetails")



}
