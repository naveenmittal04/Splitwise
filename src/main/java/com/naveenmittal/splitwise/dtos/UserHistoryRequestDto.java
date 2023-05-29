package com.naveenmittal.splitwise.dtos;

import com.naveenmittal.splitwise.models.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserHistoryRequestDto {
    private Long userId;
}
