package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.TransactionEntity;
import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
/*TODO: ADD SORTING*/
@NoRepositoryBean
public interface TransactionBaseRepository <T extends TransactionEntity> extends JpaRepository<T, Long> {
    List<T> findByUserUid(Long uid);
    Optional<T> findByIdAndUserUidAndAndCategory_CategoryType(Long id, Long uid, CategoryType catType);

}
