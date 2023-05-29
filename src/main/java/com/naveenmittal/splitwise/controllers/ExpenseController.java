package com.naveenmittal.splitwise.controllers;

import com.naveenmittal.splitwise.dtos.AddExpenseRequestDto;
import com.naveenmittal.splitwise.dtos.AddExpenseResponseDto;
import com.naveenmittal.splitwise.dtos.ResponseStatus;
import com.naveenmittal.splitwise.models.Expense;
import com.naveenmittal.splitwise.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }
    public AddExpenseResponseDto addExpense(AddExpenseRequestDto request){
        AddExpenseResponseDto response = new AddExpenseResponseDto();
        try{
            Expense expense = expenseService.addExpense(
                    request.getGroupId(),
                    request.getAmount(),
                    request.getAmountOwedBy(),
                    request.getAmountPaidBy(),
                    request.getCreatedById(),
                    request.getDescription()
            );
            response.setExpenseId(expense.getId());
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e){
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }
}
