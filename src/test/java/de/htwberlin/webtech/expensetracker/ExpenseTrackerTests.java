package de.htwberlin.webtech.expensetracker;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.entities.TransactionEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.UserEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.TransactionRepository;
import de.htwberlin.webtech.expensetracker.web.model.CategoryJSON;
import de.htwberlin.webtech.expensetracker.web.model.User;
import de.htwberlin.webtech.expensetracker.web.model.UserManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.CategoryService;
import de.htwberlin.webtech.expensetracker.web.service.TransactionService;
import de.htwberlin.webtech.expensetracker.web.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Iterables;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
@SpringBootTest
public class ExpenseTrackerTests {
    @InjectMocks
    private UserService userService;
    @InjectMocks
    private CategoryService categoryService;

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserService userService1;
    private final   UserEntity user = new UserEntity(1L, "patata@a.a", "12345");


    @Test
    @DisplayName("Should transform UserManipulationReq to UserEntity")
    void mapToUser(){
        var userManipulationReq = Mockito.mock(UserManipulationRequest.class);

        when(userManipulationReq.getEmail()).thenReturn("patata@a.a");
        when(userManipulationReq.getPassword()).thenReturn("12345");

        var result = userService.mapToUserEntity(userManipulationReq);

        assertThat(result.getEmail()).isEqualTo("patata@a.a");
        assertThat(result.getPassword()).isEqualTo("12345");

    }


    @Test
    @DisplayName("Should delete transaction by Id")
    void deleteTransaction(){
        Long tid = 77L;
        Long uid = 1L;
        CategoryEntity category = new CategoryEntity(1L, "Food and Drink", "bi-bi-icon", CategoryType.INCOME, user, Set.of());
        TransactionEntity transactionEntity =
                new TransactionEntity(category, user, 77L, "random description", new BigDecimal("0.8"), LocalDate.of(2022,01,02));
        Optional<TransactionEntity> transaction = Optional.of(transactionEntity);
        doReturn(transaction).when(transactionRepository).findByIdAndUserUid(tid, uid);
        doReturn(user).when(userService1).getLoggedInUserEntity();
        var result = transactionService.deleteTransaction(tid);
        System.out.println(result);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return category names and cids")
    void fetchCategoriesWithTransactions(){

        User user1 = new User(1L, "patata@a.a");
        String type = "expenses";
        CategoryType categoryType = CategoryType.EXPENSE;
        CategoryEntity category1 = new CategoryEntity(1L, "Food and Drink", "bi-bi-icon", CategoryType.EXPENSE, user, Set.of());
        CategoryEntity category2 = new CategoryEntity(2L, "Transportation", "bi-bi-icon", CategoryType.EXPENSE, user, Set.of());
        CategoryJSON json1 = new CategoryJSON(category1.getCid(), category1.getCategoryName(), category1.getCategoryType().toString(), category1.getIcon());
        CategoryJSON json2 = new CategoryJSON(category2.getCid(), category1.getCategoryName(), category2.getCategoryType().toString(), category2.getIcon());
        List<CategoryEntity> ents = List.of(category1, category2);
        List<CategoryJSON> l = List.of(json1, json2);
        doReturn(user1).when(userService1).getLoggedInUser();
        doReturn(ents).when(categoryRepository).findByCategoryTypeAndUserUid(categoryType, user.getUid());


        List<CategoryJSON> result = categoryService.fetchCategoryNamesPerTypeAndUser(type);
        List<String> collect = result.stream().map(el -> el.getCategoryName()).collect(Collectors.toList());
        assertThat(collect.contains(category1.getCategoryName())).isTrue();
        assertThat(collect.contains(category2.getCategoryName())).isTrue();
    }


}
