package com.example.bank.security;

import com.example.bank.model.Admin;
import com.example.bank.model.User;
import com.example.bank.repo.AdminRepo;
import com.example.bank.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepo adminRepo;

    public CustomUserDetailsService(UserRepository userRepository, AdminRepo adminRepo) {
        this.userRepository = userRepository;
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try user
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singletonList(auth));
        }

        Admin admin = adminRepo.findByEmail(username).orElse(null);
        if (admin != null) {
            GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_ADMIN");
            return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(), Collections.singletonList(auth));
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
