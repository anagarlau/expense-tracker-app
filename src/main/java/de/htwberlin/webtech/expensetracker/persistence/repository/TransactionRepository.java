package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.TransactionEntity;
import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/*TODO: ADD SORTING*/

@Repository
public interface TransactionRepository  extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserUid(Long uid);
    List<TransactionEntity> findByUserUidAndAndCategory_CategoryType( Long uid, CategoryType catType);
    Optional<TransactionEntity> findByIdAndUserUid(Long id, Long uid);
}
