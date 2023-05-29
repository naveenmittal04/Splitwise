package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.UserController;
import com.naveenmittal.splitwise.dtos.UpdateProfileRequestDto;
import com.naveenmittal.splitwise.dtos.UpdateProfileResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UpdateProfileCommand implements Command {

    private UserController userController;

    @Autowired
    public UpdateProfileCommand(UserController userController) {
        this.userController = userController;
    }
    @Override
    public boolean match(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length != 3) {
            return false;
        }
        if(!inputs[0].startsWith("u")) {
            return false;
        }
        if(!inputs[1].equals("UpdateProfile")) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length != 3) {
            System.out.println("Invalid input");
            return;
        }
        Long userId = Long.valueOf(inputs[0].substring(1));
        String password = inputs[2];

        UpdateProfileRequestDto request = new UpdateProfileRequestDto();
        request.setUserId(userId);
        request.setPassword(password);

        try {
            UpdateProfileResponseDto response = userController.updateProfile(request);
            System.out.println("User profile updated successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
