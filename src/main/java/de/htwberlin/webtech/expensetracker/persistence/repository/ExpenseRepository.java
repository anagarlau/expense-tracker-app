package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByUserUid(Long uid);
    Optional<ExpenseEntity> findByIdAndUserUidAndAndCategory_CategoryType(Long id, Long uid, CategoryType catType);
}
