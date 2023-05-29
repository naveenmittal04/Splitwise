package com.naveenmittal.splitwise.repositories;

import com.naveenmittal.splitwise.models.ExpenseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseUserRepository extends JpaRepository<ExpenseUser, Long> {

    List<ExpenseUser> findAllById(Long expenseId);

    @Override
    <S extends ExpenseUser> S save(S entity);
}
