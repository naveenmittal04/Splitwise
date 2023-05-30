package com.naveenmittal.splitwise.repositories;

import com.naveenmittal.splitwise.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllById(Long expenseId);

    List<Expense> findAllByGroupId(Long groupId);

    @Override
    Expense save(Expense entity);
}
