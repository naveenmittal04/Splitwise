package com.naveenmittal.splitwise.dtos;

import com.naveenmittal.splitwise.helper.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSettleUpResponseDto {
    private ResponseStatus status;
    private List<Transaction> transactions;
}
