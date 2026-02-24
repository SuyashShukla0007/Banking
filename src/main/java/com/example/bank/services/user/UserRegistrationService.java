package com.example.bank.services.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.model.NewUserApproval;
import com.example.bank.repo.NewUsers;
import com.example.bank.repo.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user registration operations
 */
@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final NewUsers newUsersRepo;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository, NewUsers newUsersRepo,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.newUsersRepo = newUsersRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user
     * 
     * @param request User registration details
     * @return NewUserApproval entity
     * @throws RuntimeException if email already exists
     */
    public NewUserApproval createUser(UserRegisterRequest request) {
        // checking if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        NewUserApproval user = new NewUserApproval();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");

        return newUsersRepo.save(user);
    }
}
