package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {

}
