package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByCategoryType(CategoryType categoryType);
    Optional<CategoryEntity>findByCategoryName(String categoryName);
    Optional<CategoryEntity> findByCidAndUserUidAndCategoryType(Long cid, Long uid ,CategoryType categoryType);
    List<CategoryEntity> findByCategoryTypeAndUserUid(CategoryType categoryType, Long uid);
    //TODO: findcatby users and update prior methods
    // List<CategoryEntity> findAllBy
}
