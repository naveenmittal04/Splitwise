package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileResponseDto {
    private ResponseStatus status;
    private Long userId;
}
