package com.naveenmittal.splitwise.controllers;

import com.naveenmittal.splitwise.dtos.*;
import com.naveenmittal.splitwise.helper.Transaction;
import com.naveenmittal.splitwise.services.GroupService;
import com.naveenmittal.splitwise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SettleUpController {

    private UserService userService;
    private GroupService groupService;

    @Autowired
    public SettleUpController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }
    public UserSettleUpResponseDto userSettleUp(UserSettleUpRequestDto request) {
        UserSettleUpResponseDto response = new UserSettleUpResponseDto();
        try {
            List<Transaction> transactions = userService.userSettleUp(request.getUserId());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setTransactions(transactions);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return response;
    }

    public MyTotalResponseDto myTotal(MyTotalRequestDto request) {
        MyTotalResponseDto response = new MyTotalResponseDto();
        try {
            Long amount = userService.myTotal(request.getUserId());
            response.setTotal(amount);
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return response;
    }

    public GroupSettleUpResponseDto groupSettleUp(GroupSettleUpRequestDto request) {
        GroupSettleUpResponseDto response = new GroupSettleUpResponseDto();
        try {
            List<Transaction> transactions = groupService.groupSettleUp(request.getUserId(), request.getGroupId());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setTransactions(transactions);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return response;
    }
}
