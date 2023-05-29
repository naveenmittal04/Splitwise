package com.naveenmittal.splitwise.repositories;

import com.naveenmittal.splitwise.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllById(Long userId);
}
