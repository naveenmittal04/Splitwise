package com.naveenmittal.splitwise.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Expense extends BaseModel{
    private String description;
//    @OneToMany
//    private List<ExpenseUser> expenseUsers;
    @Enumerated(value = EnumType.ORDINAL)
    private ExpenseType expenseType;
    @ManyToOne
    private Group group;
    private Long amount;
    @ManyToOne
    private User createdById;
}
