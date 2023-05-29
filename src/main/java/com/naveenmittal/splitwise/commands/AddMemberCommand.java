package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.GroupController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.naveenmittal.splitwise.dtos.AddMemberRequestDto;
import com.naveenmittal.splitwise.dtos.AddMemberResponseDto;
@Controller
public class AddMemberCommand implements Command{

    private GroupController groupController;

    @Autowired
    public AddMemberCommand(GroupController groupController) {
        this.groupController = groupController;
    }
    @Override
    public boolean match(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length != 4) {
            return false;
        }
        if(!inputs[0].startsWith("u")) {
            return false;
        }
        if(!inputs[1].equals("AddMember")) {
            return false;
        }
        if(!inputs[2].startsWith("g")) {
            return false;
        }
        if(!inputs[3].startsWith("u")) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length != 4) {
            System.out.println("Invalid input");
            return;
        }
        Long adminId = Long.valueOf(inputs[0].substring(1));
        Long groupId = Long.valueOf(inputs[2].substring(1));
        Long memberId = Long.valueOf(inputs[3].substring(1));

        AddMemberRequestDto request = new AddMemberRequestDto();
        request.setAdminId(adminId);
        request.setGroupId(groupId);
        request.setMemberId(memberId);

        AddMemberResponseDto response;

        groupController.addMemeber(request);
    }
}
