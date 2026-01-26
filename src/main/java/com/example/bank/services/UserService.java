package com.example.bank.services;

import com.example.bank.repo.UserRepo;
import org.springframework.stereotype.Service;
import com.example.bank.model.User;

@Service
public class UserService {

    UserRepo userRepo;

//create user service methods
    public  void createUser(User user) {
    userRepo.save(user);
    }
    //delete user by id
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    //get user by id
    public User getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    //get ballance by id
    public Double getBalanceById(Long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            return user.getBalance();
        }
        return null;
    }





}
