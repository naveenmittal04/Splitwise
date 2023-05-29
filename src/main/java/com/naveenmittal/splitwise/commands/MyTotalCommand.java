package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.SettleUpController;
import com.naveenmittal.splitwise.dtos.MyTotalRequestDto;
import com.naveenmittal.splitwise.dtos.MyTotalResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MyTotalCommand implements Command {

    private SettleUpController settleUpController;

    @Autowired
    public MyTotalCommand(SettleUpController settleUpController) {
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
        if(!inputs[1].equals("MyTotal")) {
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

        MyTotalRequestDto request = new MyTotalRequestDto();
        request.setUserId(userId);

        MyTotalResponseDto response = settleUpController.myTotal(request);
        Long amount = response.getTotal();
        if(amount < 0){
            System.out.println("You will pay " + (-1*amount));
        } else if(amount > 0) {
            System.out.println("You will get " + amount);
        } else {
            System.out.println("You are settled up");
        }
    }
}
