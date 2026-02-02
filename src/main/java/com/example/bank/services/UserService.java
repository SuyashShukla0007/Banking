package com.example.bank.services;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.repo.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.example.bank.model.User;

@Service
public class UserService {

    userRepository userRepository;

    public UserService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    // create user service methods
    public User createUser(UserRegisterRequest request) {

        // checking if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // hash password using bCrypt

        String rawPassword = request.getPassword();
        String hashPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        // further improvement on later stages
        user.setPassword(hashPassword);
        user.setRole("USER");

        return userRepository.save(user);
    }

    // delete user by id
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // get user by id
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // login
    public Long login(String password, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return user.getId();
    }

}
