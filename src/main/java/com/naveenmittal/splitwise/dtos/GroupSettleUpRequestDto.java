package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupSettleUpRequestDto {
    private Long groupId;
    private Long userId;
}
