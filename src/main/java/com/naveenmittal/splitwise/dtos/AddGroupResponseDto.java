package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGroupResponseDto {
    private ResponseStatus status;
    private Long groupId;
}
