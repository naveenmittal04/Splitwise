package com.naveenmittal.splitwise.commands;

import com.naveenmittal.splitwise.controllers.ExpenseController;
import com.naveenmittal.splitwise.controllers.GroupController;
import com.naveenmittal.splitwise.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AddExpenseCommand implements Command{

    private GroupController groupController;
    private ExpenseController expenseController;

    @Autowired
    public AddExpenseCommand(GroupController groupController, ExpenseController expenseController) {
        this.groupController = groupController;
        this.expenseController = expenseController;
    }

    @Override
    public boolean match(String input) {
        String[] inputs = input.split(" ");
        if(inputs.length < 4) {
            return false;
        }
        if(!inputs[0].startsWith("u")) {
            return false;
        }
        if(!inputs[1].equals("Expense")) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @Override
    public void execute(String input) {
        String[] inputs = input.split(" ");
        Long userId = Long.valueOf(inputs[0].substring(1));
        int index = 2;
        Long groupId = null;
        List<Long> memberIds = new ArrayList<>();
        if(inputs[index].startsWith("g")) {
            groupId = Long.valueOf(inputs[index].substring(1));
            GetGroupMembersRequestDto request = new GetGroupMembersRequestDto();
            request.setGroupId(groupId);
            GetGroupMembersResponseDto response = groupController.getGroupMembers(request);
            memberIds = response.getMemberIds();
            if(memberIds != null) {
                memberIds.sort(Long::compareTo);
            }else{
                memberIds.add(userId);
            }
            index++;
        } else {
            memberIds.add(userId);
            for(index = 2; index < inputs.length; index++) {
                if(inputs[index].startsWith("u")) {
                    memberIds.add(Long.valueOf(inputs[index].substring(1)));
                } else {
                    break;
                }
            }
        }
        Long totalAmountPaid = 0L;

        String whoPaid = inputs[index];
        Map<Long,Long> amountPaidBy = new HashMap<>();
        index++;
        if(whoPaid.equals("iPay")){
            totalAmountPaid = Long.valueOf(inputs[index]);index++;
            amountPaidBy.put(userId, totalAmountPaid);

        } else if(whoPaid.equals("MultiPay")) {
            int memberId = 0;
            while(isNumeric(inputs[index])) {
                Long amountPaid = Long.valueOf(inputs[index]);index++;
                totalAmountPaid += amountPaid;
                amountPaidBy.put(memberIds.get(memberId), amountPaid);
                memberId++;
            }
        }
        String splitType = inputs[index];index++;

        Map<Long,Long> amountOwedBy = new HashMap<>();
        if(splitType.equals("Exact")){
            int memberId = 0;
            while(isNumeric(inputs[index])) {
                Long amountOwed = Long.valueOf(inputs[index]);index++;
                amountOwedBy.put(memberIds.get(memberId), amountOwed);
                memberId++;
            }
        } else if(splitType.equals("Percent")) {
            int memberId = 0;
            while(isNumeric(inputs[index])) {
                Long percentageOwed = Long.valueOf(inputs[index]);index++;
                amountOwedBy.put(memberIds.get(memberId), percentageOwed*totalAmountPaid/100);
                memberId++;
            }
        } else if(splitType.equals("Equal")) {
            int memberId = 0;
            assert memberIds != null;
            int size = memberIds.size();
            while(memberId < size) {
                //Long percentageOwed = Long.valueOf(inputs[index]);index++;
                amountOwedBy.put(memberIds.get(memberId), totalAmountPaid/size);
                memberId++;
            }
        } else if(splitType.equals("Ratio")) {
            int memberId = 0;
            int size = memberIds.size();
            List<Long> ratios = new ArrayList<>();
            Long totalRatio = 0L;
            while(isNumeric(inputs[index])) {
                Long ratio = Long.valueOf(inputs[index]);index++;
                ratios.add(ratio);
                totalRatio += ratio;
            }
            while(memberId < size) {
                amountOwedBy.put(memberIds.get(memberId), totalAmountPaid*ratios.get(memberId)/totalRatio);
                memberId++;
            }
        }

        String descritpion = "";
        if(inputs[index++].equals("Desc")){
            while(index < inputs.length) {
                descritpion += inputs[index++] + " ";
            }
        }

        AddExpenseRequestDto request = new AddExpenseRequestDto();
        request.setCreatedById(userId);
        request.setAmount(totalAmountPaid);
        request.setAmountPaidBy(amountPaidBy);
        request.setAmountOwedBy(amountOwedBy);
        request.setDescription(descritpion);
        request.setGroupId(groupId);

        System.out.println("Request: ");
        System.out.println("userId: " + userId);
        System.out.println("groupId: " + groupId);
        System.out.println("totalAmountPaid: " + totalAmountPaid);
        System.out.println("amountPaidBy: ");
        for(Long paidBy: amountPaidBy.keySet()) {
            System.out.println("paidBy: " + paidBy + " amount: " + amountPaidBy.get(paidBy));
        }
        System.out.println("amountOwedBy: " );
        for(Long owedBy: amountOwedBy.keySet()) {
            System.out.println("owedBy: " + owedBy + " amount: " + amountOwedBy.get(owedBy));
        }
        System.out.println("descritpion: " + descritpion);

        AddExpenseResponseDto response = expenseController.addExpense(request);
        if(response.getStatus() == ResponseStatus.SUCCESS){
            System.out.println("Expense added successfully ExpenseId: " + response.getExpenseId());
        } else {
            System.out.println("Expense could not be added");
        }
    }

}
