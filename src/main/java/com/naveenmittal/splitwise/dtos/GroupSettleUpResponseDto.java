package com.naveenmittal.splitwise.dtos;

import com.naveenmittal.splitwise.helper.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GroupSettleUpResponseDto {
    private ResponseStatus status;
    private List<Transaction> transactions;
}
