package de.htwberlin.webtech.expensetracker.web.repository;

import de.htwberlin.webtech.expensetracker.persistence.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
}
