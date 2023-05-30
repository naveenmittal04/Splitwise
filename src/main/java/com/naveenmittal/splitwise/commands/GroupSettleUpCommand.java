package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.SettleUpController;
import com.naveenmittal.splitwise.dtos.GroupSettleUpRequestDto;
import com.naveenmittal.splitwise.dtos.GroupSettleUpResponseDto;
import com.naveenmittal.splitwise.controllers.GroupController;
import com.naveenmittal.splitwise.dtos.ResponseStatus;
import com.naveenmittal.splitwise.helper.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GroupSettleUpCommand implements Command{
    private SettleUpController settleUpController;

    @Autowired
    public GroupSettleUpCommand(SettleUpController settleUpController) {
        this.settleUpController = settleUpController;
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
        if(!inputs[1].equals("SettleUp")) {
            return false;
        }
        if(!inputs[2].startsWith("g")) {
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
        Long groupId = Long.valueOf(inputs[2].substring(1));

        GroupSettleUpRequestDto request = new GroupSettleUpRequestDto();
        request.setUserId(userId);
        request.setGroupId(groupId);

        GroupSettleUpResponseDto response = settleUpController.groupSettleUp(request);

        if(response.getStatus().equals(ResponseStatus.SUCCESS)){
            System.out.println("Settle up successful");
            for(Transaction t: response.getTransactions()) {
                System.out.println("u"+t.getFrom().getId() + " owes u" + t.getTo().getId() + " " + t.getAmount());
            }
        } else {
            System.out.println("Settle up failed");
        }

    }
}
