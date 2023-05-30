package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.UserController;
import com.naveenmittal.splitwise.dtos.ResponseStatus;
import com.naveenmittal.splitwise.dtos.UserHistoryRequestDto;
import com.naveenmittal.splitwise.dtos.UserHistoryResponseDto;
import com.naveenmittal.splitwise.models.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserHistoryCommand implements Command{

    private UserController userController;

    @Autowired
    public UserHistoryCommand(UserController userController) {
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
        if(!inputs[1].equals("History")) {
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

        UserHistoryRequestDto request = new UserHistoryRequestDto();
        request.setUserId(userId);

        UserHistoryResponseDto response = userController.userHistory(request);
        if(response.getStatus().equals(ResponseStatus.SUCCESS)){
            for(Expense expense: response.getExpenses()){
                System.out.println("Expense "+ expense.getId() + " "+ expense.getDescription() + " "+ expense.getAmount());
            }
        } else {
            System.out.println("Failed to get User Expense History");
        }
    }
}
