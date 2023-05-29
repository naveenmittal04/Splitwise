package com.naveenmittal.splitwise.controllers;

import com.naveenmittal.splitwise.dtos.*;
import com.naveenmittal.splitwise.helper.Transaction;
import com.naveenmittal.splitwise.models.Group;
import com.naveenmittal.splitwise.models.User;
import com.naveenmittal.splitwise.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GroupController {
    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public AddGroupResponseDto addGroup(AddGroupRequestDto request) {
        AddGroupResponseDto response = new AddGroupResponseDto();
        try {
            Group group = groupService.addGroup(request.getName(), request.getAdminId());
            response.setGroupId(group.getId());
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
        }

        return response;
    }

    public AddMemberResponseDto addMemeber(AddMemberRequestDto request){
        AddMemberResponseDto response = new AddMemberResponseDto();
        try{
            Group group = groupService.addMember(request.getAdminId(), request.getGroupId(), request.getMemberId());
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return null;
    }

    public GetGroupMembersResponseDto getGroupMembers(GetGroupMembersRequestDto request) {
        GetGroupMembersResponseDto response = new GetGroupMembersResponseDto();
        try {
            List<User> members = groupService.getGroupMembers(request.getGroupId());
            List<Long> memberIds = new ArrayList<>();
            for (User member : members) {
                memberIds.add(member.getId());
            }
            response.setMemberIds(memberIds);
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILURE);
            System.out.println(e.getMessage());
        }
        return response;
    }
}
