package com.naveenmittal.splitwise.services;

import com.naveenmittal.splitwise.exceptions.UserExistException;
import com.naveenmittal.splitwise.exceptions.UserNotFoundException;
import com.naveenmittal.splitwise.helper.Transaction;
import com.naveenmittal.splitwise.models.*;
import com.naveenmittal.splitwise.repositories.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;

    private ExpenseUserRepository expenseUserRepository;

    private GroupRepository groupRepository;

    private ExpenseRepository expenseRepository;

    @Autowired
    public UserService(UserRepository userRepository, ExpenseUserRepository expenseUserRepository, GroupRepository groupRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseUserRepository = expenseUserRepository;
        this.groupRepository = groupRepository;
        this.expenseRepository = expenseRepository;
    }
    public User registerUser(String name, String phoneNumber, String password) throws UserExistException {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if(userOptional.isPresent()){
            throw new UserExistException();
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(password);

        User savedUser = userRepository.save(newUser);
        return savedUser;
    }

    public List<Transaction> userSettleUp(Long userId) throws Exception {

        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }

        User user = userOptional.get();

        List<Expense> expenses = userHistory(userId);
        System.out.println("Expenses : " + expenses.size());
        Map<Long, User> usersMap = new HashMap<>();
        Map<Long, Long> userExtraPaidMap = new HashMap<>();
        for(Expense expense: expenses){
            try{
                List<ExpenseUser> expenseUsers = expenseUserRepository.findAllByExpenseId(expense.getId());
                for(ExpenseUser expenseUser: expenseUsers){
                    Long amount = expenseUser.getAmount();
                    if(amount == null){
                        throw new Exception("Amount cann't be null, Something is Wrong");
                    }
                    if(expenseUser.getExpenseUserType().equals(ExpenseUserType.OWED_BY)){
                        amount = -1*amount;
                    }
                    if(userExtraPaidMap.containsKey(expenseUser.getUser().getId())){
                        userExtraPaidMap.put(expenseUser.getUser().getId(), userExtraPaidMap.get(expenseUser.getUser().getId()) + amount);
                    } else {
                        userExtraPaidMap.put(expenseUser.getUser().getId(), amount);
                    }
                    usersMap.put(expenseUser.getUser().getId(), expenseUser.getUser());
                   // System.out.println(expenseUser.getUser().toString() + userExtraPaidMap.get(expenseUser.getUser().getId()));
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }

        Long amountExtraPaidByUser = userExtraPaidMap.get(user.getId());
        if(amountExtraPaidByUser == null){
            throw new Exception("amountExtraPaidByUser cann't be null, Something is Wrong");
        }
        boolean doesUserOwe = amountExtraPaidByUser < 0;
        PriorityQueue<Pair<Long, Long>> pq;
        if(doesUserOwe){
            pq = new PriorityQueue<>((a, b) -> Math.toIntExact(b.getKey() - a.getKey()));
            for(Map.Entry<Long, Long> entry: userExtraPaidMap.entrySet()){
                if(entry.getValue() > 0){
                    pq.add(Pair.of(entry.getValue(), entry.getKey()));
                }
            }
        } else {
            pq = new PriorityQueue<>((a, b) -> Math.toIntExact(a.getKey() - b.getKey()));
            for(Map.Entry<Long, Long> entry: userExtraPaidMap.entrySet()){
                if(entry.getValue() < 0){
                    pq.add(Pair.of(entry.getValue(), entry.getKey()));
                }
            }
        }

        List<Transaction> transactions = new ArrayList<>();
        while(amountExtraPaidByUser != 0 && !pq.isEmpty()){
            Long amount = pq.peek().getKey();
            Transaction transaction = new Transaction();
            if(doesUserOwe){
                if(amountExtraPaidByUser + amount > 0){
                    transaction.setAmount(-amountExtraPaidByUser);
                    transaction.setFrom(user);
                    transaction.setTo(usersMap.get(pq.peek().getValue()));
                    amountExtraPaidByUser = 0L;
                } else {
                    transaction.setAmount(amount);
                    transaction.setFrom(user);
                    transaction.setTo(usersMap.get(pq.peek().getValue()));
                    amountExtraPaidByUser = amountExtraPaidByUser + amount;
                    pq.poll();
                }
            } else {
                if(amountExtraPaidByUser + amount < 0){
                    transaction.setAmount(amountExtraPaidByUser);
                    transaction.setTo(user);
                    transaction.setFrom(usersMap.get(pq.peek().getValue()));
                    amountExtraPaidByUser = 0L;
                } else {
                    transaction.setAmount(Math.abs(amount));
                    transaction.setTo(user);
                    transaction.setFrom(usersMap.get(pq.peek().getValue()));
                    amountExtraPaidByUser = amountExtraPaidByUser + amount;
                    pq.poll();
                }
            }
            System.out.println("Transaction : "+transaction.getFrom().getName() + " " + transaction.getTo().getName() + " " + transaction.getAmount());
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
        List<ExpenseUser> expensesByUser = expenseUserRepository.findAllByUserId(userId);
        Long total = 0L;
        for (ExpenseUser expenseByUser: expensesByUser) {
            if (expenseByUser.getExpenseUserType().equals(ExpenseUserType.OWED_BY)) {
                total -= expenseByUser.getAmount();
            } else {
                total += expenseByUser.getAmount();
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
        List<ExpenseUser> expenseUsers = expenseUserRepository.findAllByUserId(userId);
        Set<Expense> expenseSet = new HashSet<>();
        for (ExpenseUser expenseUser: expenseUsers) {
            expenseSet.add(expenseUser.getExpense());
        }
        List<Expense> expenses = new ArrayList<>(expenseSet);
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
