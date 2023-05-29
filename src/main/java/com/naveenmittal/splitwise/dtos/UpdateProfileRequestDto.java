package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProfileRequestDto {
    private Long userId;
    private String password;
}
