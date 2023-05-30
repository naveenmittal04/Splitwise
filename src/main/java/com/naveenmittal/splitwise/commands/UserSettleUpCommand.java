package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.SettleUpController;
import com.naveenmittal.splitwise.dtos.ResponseStatus;
import com.naveenmittal.splitwise.dtos.UserSettleUpRequestDto;
import com.naveenmittal.splitwise.dtos.UserSettleUpResponseDto;
import com.naveenmittal.splitwise.helper.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserSettleUpCommand implements Command{

    private SettleUpController settleUpController;

    @Autowired
    public UserSettleUpCommand(SettleUpController settleUpController) {
        this.settleUpController = settleUpController;
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
        if(!inputs[1].equals("SettleUp")) {
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

        UserSettleUpRequestDto userSettleUpRequestDto = new UserSettleUpRequestDto();
        userSettleUpRequestDto.setUserId(userId);
        try {
            UserSettleUpResponseDto response = settleUpController.userSettleUp(userSettleUpRequestDto);
            if(response.getStatus() == ResponseStatus.SUCCESS) {
                for(Transaction t: response.getTransactions()) {
                    System.out.println("u"+t.getFrom().getId() + " owes u" + t.getTo().getId() + " " + t.getAmount());
                }
            } else {
                System.out.println("User settle up failed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return;
    }
}
