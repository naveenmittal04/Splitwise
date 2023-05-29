package com.naveenmittal.splitwise.dtos;

import com.naveenmittal.splitwise.models.Expense;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserHistoryResponseDto {
    private ResponseStatus status;
    private List<Expense> expenses;
}
