package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AddExpenseRequestDto {
    private Long createdById;
    private String description;
    private Long amount;
    private Long groupId;
    private Map<Long,Long> amountPaidBy;
    private Map<Long,Long> amountOwedBy;
}
