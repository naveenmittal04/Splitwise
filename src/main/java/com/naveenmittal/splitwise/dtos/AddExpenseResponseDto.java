package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddExpenseResponseDto {
    private ResponseStatus status;
    private Long expenseId;
}
