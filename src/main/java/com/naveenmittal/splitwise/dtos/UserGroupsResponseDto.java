package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserGroupsResponseDto {
    private ResponseStatus status;
    private List<Long> groupIds;
    private List<String> groupNames;
}
