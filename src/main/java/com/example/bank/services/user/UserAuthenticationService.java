package com.example.bank.services.user;

import com.example.bank.model.User;
import com.example.bank.repo.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user authentication operations
 */
@Service
public class UserAuthenticationService {

    private final UserRepository userRepository;

    public UserAuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticate user with email and password
     * 
     * @param email    User email
     * @param password User password
     * @return User ID if authentication successful
     * @throws RuntimeException if credentials are invalid
     */
    public Long login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        return user.getId();
    }
}
