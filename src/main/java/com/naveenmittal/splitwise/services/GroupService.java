package com.naveenmittal.splitwise.services;

import com.naveenmittal.splitwise.exceptions.*;
import com.naveenmittal.splitwise.helper.Transaction;
import com.naveenmittal.splitwise.models.*;
import com.naveenmittal.splitwise.repositories.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
    private GroupRepository groupRepository;
    private UserRepository userRepository;

    private ExpenseRepository expenseRepository;

    private ExpenseUserRepository expenseUserRepository;

    private GroupMemberRepository groupMemberRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository, ExpenseRepository expenseRepository, ExpenseUserRepository expenseUserRepository, GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.expenseUserRepository = expenseUserRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public Group addGroup(String name, Long adminId) throws GroupAlreadyExistsException, UserNotFoundException {
        Optional<Group> groupOptional = groupRepository.findByName(name);
        if(groupOptional.isPresent()){
            throw new GroupAlreadyExistsException();
        }
        Optional<User> adminOptional = userRepository.findById(adminId);
        if(!adminOptional.isPresent()){
            throw new UserNotFoundException();
        }
        Group group = new Group();
        List<User> members = new ArrayList<User>();
        members.add(adminOptional.get());
        group.setName(name);
        group.setAdmin(adminOptional.get());
        group.setMembers(members);
        return groupRepository.save(group);
    }

    public Group addMember(Long adminId, Long groupId, Long memberId) throws UserNotFoundException, OnlyGroupAdminCanAddMemberException, GroupNotFoundException {
        Optional<User> adminOptional = userRepository.findById(adminId);
        if(adminOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if(groupOptional.isEmpty()){
            throw new GroupNotFoundException();
        }
        Group group = groupOptional.get();
        if(!Objects.equals(group.getAdmin().getId(), adminId)){
            throw new OnlyGroupAdminCanAddMemberException();
        }
        Optional<User> memberOptional = userRepository.findById(memberId);
        if(memberOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        List<User> members = group.getMembers();
        members.add(memberOptional.get());
        group.setMembers(members);
        return groupRepository.save(group);
    }

    public List<Transaction> groupSettleUp(Long userId, Long groupId) throws UserNotFoundException, GroupNotFoundException, UserNotAMemberOfGroupException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if(groupOptional.isEmpty()){
            throw new GroupNotFoundException();
        }

        Group group = groupOptional.get();
        List<User> members = group.getMembers();
        if(!members.contains(userOptional.get())){
           throw new UserNotAMemberOfGroupException();
        }

        List<Expense> expenses = expenseRepository.findAllById(groupId);

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

//        Long amountExtraPaidByUser = userExtraPaidMap.get(user);
//        boolean doesUserOwe = amountExtraPaidByUser < 0;
//        PriorityQueue<Pair<Long, User>> pq;
//        if(doesUserOwe){
//            pq = new PriorityQueue<>((a, b) -> Math.toIntExact(b.getKey() - a.getKey()));
//        } else {
//            pq = new PriorityQueue<>((a, b) -> Math.toIntExact(a.getKey() - b.getKey()));
//        }

        PriorityQueue<Pair<Long, User>> maxHeap = new PriorityQueue<>((a, b) -> Math.toIntExact(b.getKey() - a.getKey()));
        PriorityQueue<Pair<Long, User>> minHeap = new PriorityQueue<>((a, b) -> Math.toIntExact(a.getKey() - b.getKey()));
        for(Map.Entry<User, Long> entry: userExtraPaidMap.entrySet()){
            if(entry.getValue() > 0L){
                maxHeap.add(Pair.of(entry.getValue(), entry.getKey()));
            } else {
                minHeap.add(Pair.of(entry.getValue(), entry.getKey()));
            }
        }

        List<Transaction> transactions = new ArrayList<>();
        while(!minHeap.isEmpty() || !maxHeap.isEmpty()){
            Transaction transaction = new Transaction();
            Pair<Long, User> maxHeapTop = maxHeap.poll();
            Pair<Long, User> minHeapTop = minHeap.poll();
            if(maxHeapTop.getKey() > Math.abs(minHeapTop.getKey())){
                // minHeapTop will pay maxHeapTop
                transaction.setAmount(Math.abs(minHeapTop.getKey()));
                transaction.setFrom(minHeapTop.getValue());
                transaction.setTo(maxHeapTop.getValue());
                maxHeap.add(Pair.of(maxHeapTop.getKey() + minHeapTop.getKey(), maxHeapTop.getValue()));
            } else {
                // maxHeapTop will pay minHeapTop
                transaction.setAmount(maxHeapTop.getKey());
                transaction.setFrom(minHeapTop.getValue());
                transaction.setTo(maxHeapTop.getValue());
                minHeap.add(Pair.of(maxHeapTop.getKey() + minHeapTop.getKey(), minHeapTop.getValue()));
            }
            transactions.add(transaction);
        }

        return transactions;
    }

    public List<User> getGroupMembers(Long groupId) throws GroupNotFoundException {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if(groupOptional.isEmpty()){
            throw new GroupNotFoundException();
        }
        Group group = groupOptional.get();
        List<User> members = group.getMembers();
        return members;
    }
}
