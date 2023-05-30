package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
    private String name;
    private String phoneNumber;
    private String password;
}
