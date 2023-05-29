package com.naveenmittal.splitwise.helper;

import com.naveenmittal.splitwise.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private User from;
    private User to;
    private Long amount;
}
