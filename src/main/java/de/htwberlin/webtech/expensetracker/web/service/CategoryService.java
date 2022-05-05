package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.CategoryJSON;
import de.htwberlin.webtech.expensetracker.web.model.CategoryManipulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    private ExpenseService expenseService;
    private UserService userService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ExpenseService expenseService, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.expenseService = expenseService;
        this.userService = userService;
    }


     public List<CategoryJSON> fetchByCategoryNamesByType(String type){
        List<CategoryJSON> catNames = new ArrayList<>();
        if(type.equals("expenses"))
            catNames = this.categoryRepository.findByCategoryTypeAndUserUid(CategoryType.EXPENSE, userService.getLoggedInUser().getUid())
                    .stream().map(cat -> new CategoryJSON(cat.getCid(), cat.getCategoryName())).collect(Collectors.toList());
        else if(type.equals("incomes"))
            catNames =  this.categoryRepository.findByCategoryTypeAndUserUid(CategoryType.INCOME, userService.getLoggedInUser().getUid())
                    .stream()
                    .map(cat -> new CategoryJSON(cat.getCid(), cat.getCategoryName())).collect(Collectors.toList());
         return catNames;
     }


    public Category createCategory(CategoryManipulationRequest categoryRequest) {
       CategoryEntity savedCategory = this.categoryRepository.save( new CategoryEntity(userService.getLoggedInUserEntity(),categoryRequest.getCategoryName(), CategoryType.valueOf(categoryRequest.getCategoryType())));

        if (savedCategory.getCid() > 0) return mapToCategory(savedCategory);
        else return null;
    }


    private Category mapToCategory(CategoryEntity categoryEntity){
        List<Long> expensesIds = categoryEntity.getExpenses().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        List<Long> incomesIds = categoryEntity.getIncomes().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        return new Category( categoryEntity.getCid(), this.userService.getLoggedInUser().getUid(),categoryEntity.getCategoryName(), categoryEntity.getCategoryType().name(),expensesIds, incomesIds);
    }


}
