package com.naveenmittal.splitwise.controllers;

import com.naveenmittal.splitwise.dtos.*;

import com.naveenmittal.splitwise.models.Expense;
import com.naveenmittal.splitwise.models.Group;
import com.naveenmittal.splitwise.models.User;
import com.naveenmittal.splitwise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public RegisterResponseDto register(RegisterRequestDto request) {
        RegisterResponseDto response = new RegisterResponseDto();
        try {
            User user = userService.registerUser(request.getName(), request.getPhoneNumber(), request.getPassword());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setUserId(user.getId());
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }

    public UpdateProfileResponseDto updateProfile(UpdateProfileRequestDto request) {
        UpdateProfileResponseDto response = new UpdateProfileResponseDto();
        try {
            User user = userService.updateProfile(request.getUserId(), request.getPassword());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setUserId(user.getId());
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
        }
        return response;
    }

    public UserHistoryResponseDto userHistory(UserHistoryRequestDto request) {
        UserHistoryResponseDto response = new UserHistoryResponseDto();
        try {
            List<Expense> expenses = userService.userHistory(request.getUserId());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setExpenses(expenses);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return response;
    }

    public UserGroupsResponseDto userGroups(UserGroupsRequestDto request) {
        UserGroupsResponseDto response = new UserGroupsResponseDto();
        try {
            List<Group> groups = userService.userGroups(request.getUserId());
            List<Long> groupIds = new ArrayList<>();
            List<String> groupNames = new ArrayList<>();
            for(Group group: groups) {
                groupIds.add(group.getId());
                groupNames.add(group.getName());
            }
            response.setGroupIds(groupIds);
            response.setGroupNames(groupNames);
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return response;
    }
}
