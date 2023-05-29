package com.naveenmittal.splitwise.services;

import com.naveenmittal.splitwise.exceptions.UserExistException;
import com.naveenmittal.splitwise.exceptions.UserNotFoundException;
import com.naveenmittal.splitwise.helper.Transaction;
import com.naveenmittal.splitwise.models.*;
import com.naveenmittal.splitwise.repositories.ExpenseUserRepository;
import com.naveenmittal.splitwise.repositories.GroupRepository;
import com.naveenmittal.splitwise.repositories.UserExpenseRepository;
import com.naveenmittal.splitwise.repositories.UserRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserExpenseRepository userExpenseRepository;

    private ExpenseUserRepository expenseUserRepository;

    private GroupRepository groupRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserExpenseRepository userExpenseRepository, ExpenseUserRepository expenseUserRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.userExpenseRepository = userExpenseRepository;
        this.expenseUserRepository = expenseUserRepository;
        this.groupRepository = groupRepository;
    }
    public User registerUser(String phoneNumber, String password) throws UserExistException {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if(userOptional.isPresent()){
            throw new UserExistException();
        }
        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(password);

        User savedUser = userRepository.save(newUser);
        return savedUser;
    }

    public List<Transaction> userSettleUp(Long userId) throws UserNotFoundException {

        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()){
            throw new UserNotFoundException();
        }

        User user = userOptional.get();

        List<Expense> expenses = userExpenseRepository.findAllById(userId);

        Map<User, Long> userExtraPaidMap = new HashMap<>();
        for(Expense expense: expenses){
            List<ExpenseUser> expenseUsers = expenseUserRepository.findAllById(expense.getId());
            for(ExpenseUser expenseUser: expenseUsers){
                Long amount = expenseUser.getAmount();
                if(expenseUser.getExpenseUserType().equals(ExpenseUserType.OWED_BY)){
                    amount = -amount;
                }
                if(userExtraPaidMap.containsKey(expenseUser.getUser())){
                    userExtraPaidMap.put(expenseUser.getUser(), userExtraPaidMap.get(expenseUser.getUser()) + amount);
                } else {
                    userExtraPaidMap.put(expenseUser.getUser(), amount);
                }
            }
        }

        Long amountExtraPaidByUser = userExtraPaidMap.get(user);
        boolean doesUserOwe = amountExtraPaidByUser < 0;
        PriorityQueue<Pair<Long, User>> pq;
        if(doesUserOwe){
            pq = new PriorityQueue<>((a, b) -> Math.toIntExact(b.getKey() - a.getKey()));
        } else {
            pq = new PriorityQueue<>((a, b) -> Math.toIntExact(a.getKey() - b.getKey()));
        }

        List<Transaction> transactions = new ArrayList<>();
        while(amountExtraPaidByUser != 0){
            Long amount = pq.peek().getKey();
            Transaction transaction = new Transaction();
            if(doesUserOwe){
                if(amountExtraPaidByUser + amount > 0){
                    transaction.setAmount(-amountExtraPaidByUser);
                    transaction.setFrom(user);
                    transaction.setTo(pq.peek().getValue());
                    amountExtraPaidByUser = 0L;
                } else {
                    transaction.setAmount(amount);
                    transaction.setFrom(user);
                    transaction.setTo(pq.peek().getValue());
                    amountExtraPaidByUser = amountExtraPaidByUser + amount;
                }
            } else {
                if(amountExtraPaidByUser + amount < 0){
                    transaction.setAmount(amountExtraPaidByUser);
                    transaction.setTo(user);
                    transaction.setFrom(pq.peek().getValue());
                    amountExtraPaidByUser = 0L;
                } else {
                    transaction.setAmount(Math.abs(amount));
                    transaction.setTo(user);
                    transaction.setFrom(pq.peek().getValue());
                    amountExtraPaidByUser = amountExtraPaidByUser + amount;
                }
            }
            transactions.add(transaction);
        }

        return transactions;
    }

    public User updateProfile(Long userId, String password) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()){
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        user.setPassword(password);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public Long myTotal(Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        List<Expense> expenses = userExpenseRepository.findAllById(userId);
        Long total = 0L;
        for (Expense expense: expenses) {
            List<ExpenseUser> expenseUsers = expenseUserRepository.findAllById(expense.getId());
            for (ExpenseUser expenseUser: expenseUsers) {
                if (expenseUser.getUser().equals(user)) {
                    if (expenseUser.getExpenseUserType().equals(ExpenseUserType.OWED_BY)) {
                        total -= expenseUser.getAmount();
                    } else {
                        total += expenseUser.getAmount();
                    }
                }
            }
        }
        return total;
    }

    public List<Expense> userHistory(Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        List<Expense> expenses = userExpenseRepository.findAllById(userId);

        return expenses;
    }

    public List<Group> userGroups(Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        List<Group> groups = groupRepository.findAllById(userId);
        return groups;
    }
}
