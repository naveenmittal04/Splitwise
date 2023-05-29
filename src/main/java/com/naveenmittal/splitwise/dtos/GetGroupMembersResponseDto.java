package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetGroupMembersResponseDto {
    private ResponseStatus status;
    private List<Long> memberIds;
}
