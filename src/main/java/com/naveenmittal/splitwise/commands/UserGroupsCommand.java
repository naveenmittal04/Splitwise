package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.UserController;
import com.naveenmittal.splitwise.dtos.ResponseStatus;
import com.naveenmittal.splitwise.dtos.UserGroupsRequestDto;
import com.naveenmittal.splitwise.dtos.UserGroupsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserGroupsCommand implements Command{

    private UserController userController;

    @Autowired
    public UserGroupsCommand(UserController userController) {
        this.userController = userController;
    }
    @Override
    public boolean match(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length != 2) {
            return false;
        }
        if(!inputs[0].startsWith("u")) {
            return false;
        }
        if(!inputs[1].equals("Groups")) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length != 2) {
            System.out.println("Invalid input");
            return;
        }
        Long userId = Long.valueOf(inputs[0].substring(1));

        UserGroupsRequestDto request = new UserGroupsRequestDto();
        request.setUserId(userId);

        UserGroupsResponseDto response = userController.userGroups(request);
        if(response.getStatus().equals(ResponseStatus.SUCCESS)) {
            System.out.println("Groups:");
            Long size = (long) response.getGroupIds().size();
            for(int i = 0; i < size; i++) {
                System.out.println(response.getGroupIds().get(i) + response.getGroupNames().get(i));
            }
        } else {
            System.out.println("Failed to fetch groups");
        }
    }
}
