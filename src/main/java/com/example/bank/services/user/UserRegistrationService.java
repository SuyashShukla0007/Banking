package com.example.bank.services.user;

import com.example.bank.DTO.User.UserRegisterRequest;
import com.example.bank.model.NewUserApproval;
import com.example.bank.repo.NewUsers;
import com.example.bank.repo.userRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user registration operations
 */
@Service
public class UserRegistrationService {

    private final userRepository userRepository;
    private final NewUsers newUsersRepo;

    public UserRegistrationService(userRepository userRepository, NewUsers newUsersRepo) {
        this.userRepository = userRepository;
        this.newUsersRepo = newUsersRepo;
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

        // hash password using bCrypt
        String rawPassword = request.getPassword();
        String hashPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        user.setPassword(hashPassword);
        user.setRole("USER");

        return newUsersRepo.save(user);
    }
}
