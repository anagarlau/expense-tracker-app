package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public interface IncomeRepository extends TransactionBaseRepository<IncomeEntity> {

}
