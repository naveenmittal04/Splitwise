package com.naveenmittal.splitwise.repositories;

import com.naveenmittal.splitwise.models.ExpenseUser;
import jakarta.persistence.UniqueConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseUserRepository extends JpaRepository<ExpenseUser, Long> {

    List<ExpenseUser> findAllById(Long expenseUserId);

    List<ExpenseUser> findAllByExpenseId(Long expenseId);

    List<ExpenseUser> findAllByUserId(Long userId);

    @Override
    <S extends ExpenseUser> S save(S entity);
}
