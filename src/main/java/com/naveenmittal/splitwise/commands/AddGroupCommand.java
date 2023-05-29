package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.GroupController;
import com.naveenmittal.splitwise.dtos.AddGroupRequestDto;
import com.naveenmittal.splitwise.dtos.AddGroupResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AddGroupCommand implements Command {

    private GroupController groupController;

    @Autowired
    public AddGroupCommand(GroupController groupController) {
        this.groupController = groupController;
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
        if(!inputs[1].equals("AddGroup")) {
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
        String groupName = inputs[2];

        AddGroupRequestDto request = new AddGroupRequestDto();
        request.setAdminId(userId);
        request.setName(groupName);

        AddGroupResponseDto response;
        try {
            response = groupController.addGroup(request);
            System.out.println("Group added successfully GroupId: "+ response.getGroupId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return;
    }
}
