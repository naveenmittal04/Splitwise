package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.UserController;
import com.naveenmittal.splitwise.dtos.RegisterRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RegisterUserCommand implements Command{
    private UserController userController;

    @Autowired
    public RegisterUserCommand(UserController userController) {
        this.userController = userController;
    }
    @Override
    public boolean match(String input) {
        return input.startsWith("Register");
    }

    @Override
    public void execute(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length != 4) {
            System.out.println("Invalid input");
            return;
        }
        String name = inputs[1];
        String phoneNumber = inputs[2];
        String password = inputs[3];
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setName(name);
        registerRequestDto.setPhoneNumber(phoneNumber);
        registerRequestDto.setPassword(password);
        try {
            userController.register(registerRequestDto);
            System.out.println("User registered successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return;
    }
}
