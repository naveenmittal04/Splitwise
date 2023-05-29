package com.naveenmittal.splitwise.services;

import com.naveenmittal.splitwise.exceptions.GroupNotFoundException;
import com.naveenmittal.splitwise.exceptions.UserNotFoundException;
import com.naveenmittal.splitwise.models.*;
import com.naveenmittal.splitwise.repositories.ExpenseRepository;
import com.naveenmittal.splitwise.repositories.GroupRepository;
import com.naveenmittal.splitwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExpenseService {

    private GroupRepository groupRepository;
    private UserRepository userRepository;

    private ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(GroupRepository groupRepository, UserRepository userRepository, ExpenseRepository expenseRepository){
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }
    public Expense addExpense(
            Long groupId,
            Long amount,
            Map<Long,Long> amountOwedBy,
            Map<Long,Long> amountPaidBy,
            Long createdById,
            String description
    ) throws GroupNotFoundException, UserNotFoundException {
        Optional<User> createdByOptional = userRepository.findById(createdById);
        if(createdByOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User craetedBy = createdByOptional.get();

        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setCreatedById(craetedBy);
        expense.setDescription(description);

        if(groupId != null) {
            Optional<Group> groupOptional = groupRepository.findById(groupId);
            if (groupOptional.isEmpty()) {
                throw new GroupNotFoundException();
            }
            expense.setGroup(groupOptional.get());
        }

        List<ExpenseUser> expenseUsers = new ArrayList<>();
        for(Map.Entry<Long, Long> entry : amountPaidBy.entrySet()){
            Optional<User> userOptional = userRepository.findById(entry.getKey());
            if(userOptional.isEmpty()){
                throw new UserNotFoundException();
            }
            ExpenseUser expenseUser = new ExpenseUser();
            expenseUser.setUser(userOptional.get());
            expenseUser.setAmount(entry.getValue());
            expenseUser.setExpenseUserType(ExpenseUserType.PAID_BY);
            expenseUsers.add(expenseUser);
        }

        for(Map.Entry<Long, Long> entry : amountOwedBy.entrySet()){
            Optional<User> userOptional = userRepository.findById(entry.getKey());
            if(userOptional.isEmpty()){
                throw new UserNotFoundException();
            }
            ExpenseUser expenseUser = new ExpenseUser();
            expenseUser.setUser(userOptional.get());
            expenseUser.setAmount(entry.getValue());
            expenseUser.setExpenseUserType(ExpenseUserType.OWED_BY);
            expenseUsers.add(expenseUser);
        }

        expense.setExpenseUsers(expenseUsers);

        Expense savedExpnese = expenseRepository.save(expense);

        return savedExpnese;
    }
}
