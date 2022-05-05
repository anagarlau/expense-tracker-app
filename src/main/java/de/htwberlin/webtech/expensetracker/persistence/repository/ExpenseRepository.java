package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ExpenseRepository extends TransactionBaseRepository<ExpenseEntity> {

}
