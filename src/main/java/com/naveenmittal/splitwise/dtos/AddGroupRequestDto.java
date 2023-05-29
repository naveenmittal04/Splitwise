package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGroupRequestDto {
    private String name;
    private Long adminId;
}
