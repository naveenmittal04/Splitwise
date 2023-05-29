package com.naveenmittal.splitwise.controllers;

import com.naveenmittal.splitwise.dtos.RegisterRequestDto;
import com.naveenmittal.splitwise.dtos.RegisterResponseDto;
import com.naveenmittal.splitwise.dtos.ResponseStatus;

import com.naveenmittal.splitwise.models.User;
import com.naveenmittal.splitwise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public RegisterResponseDto register(RegisterRequestDto request) {
        RegisterResponseDto response = new RegisterResponseDto();
        try {
            User user = userService.registerUser(request.getPhoneNumber(), request.getPassword());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setUserId(user.getId());
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }
}
