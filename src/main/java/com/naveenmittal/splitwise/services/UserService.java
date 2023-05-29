package com.naveenmittal.splitwise.services;

import com.naveenmittal.splitwise.exceptions.UserExistException;
import com.naveenmittal.splitwise.models.User;
import com.naveenmittal.splitwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User registerUser(String phoneNumber, String password) throws UserExistException {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if(userOptional.isPresent()){
            throw new UserExistException();
        }
        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(password);

        User savedUser = userRepository.save(newUser);
        return savedUser;
    }
}
