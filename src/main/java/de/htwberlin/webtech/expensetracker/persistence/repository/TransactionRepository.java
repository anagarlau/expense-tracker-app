package de.htwberlin.webtech.expensetracker.persistence.repository;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.TransactionEntity;
import de.htwberlin.webtech.expensetracker.web.model.IBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
/*TODO: ADD SORTING*/

@Repository
public interface TransactionRepository  extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserUid(Long uid);
    List<TransactionEntity> findByUserUidAndAndCategory_CategoryType( Long uid, CategoryType catType);
    Optional<TransactionEntity> findByIdAndUserUid(Long id, Long uid);
    Optional<TransactionEntity> findByCategoryCidIn(Set<Long> cids);

    @Query(value = "with expenses as \n" +
            "(select t.uid as uid, sum(transaction_total) as expenseAmount  FROM `transaction` t\n" +
            "left join category c on c.cid = t.cid\n" +
            "where t.uid=?1\n" +
            "group by  t.uid, category_type having category_type='EXPENSE' ),  \n" +
            "incomes as \n" +
            "(select t.uid as uid, sum(transaction_total) as incomeAmount  FROM `transaction` t\n" +
            "left join category c on c.cid = t.cid\n" +
            "where t.uid=?1\n" +
            "group by  t.uid, category_type having category_type='INCOME')  \n" +
            "select IFNULL(expenseAmount,0) as expenseAmount,IFNULL(incomeAmount,0) as incomeAmount, IFNULL(incomeAmount,0)-IFNULL(expenseAmount,0) as balance from  incomes left join expenses on incomes.uid = expenses.uid\n" +
            "union \n" +
            "select IFNULL(expenseAmount,0) as expenseAmount ,IFNULL(incomeAmount,0) as incomeAmount, IFNULL(incomeAmount,0)-IFNULL(expenseAmount,0) as balance from  incomes right join expenses on incomes.uid = expenses.uid;", nativeQuery = true)
    IBalance sumAcrossCategory(Long uid);




//    @Query(value = "SELECT sum(t.transactionTotal) as balance from transaction t where t.user.uid =?1 group by t.category.categoryType having t.category.categoryType='INCOME'")
//    BigDecimal sumIncomeTransactionTotalByCategoryType(Long uid);
//    @Query(value = "SELECT sum(t.transactionTotal) as balance from transaction t where t.user.uid =?1 group by t.category.categoryType having t.category.categoryType='EXPENSE'")
//    BigDecimal sumExpenseTransactionTotalByCategoryType(Long uid);
}
