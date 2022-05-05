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


//    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
//    public List<Category> fetchAllCategories(){
//        return this.categoryRepository.findAll().stream().map(categoryEntity -> this.mapToCategory(categoryEntity)).collect(Collectors.toList());
//    }

     public List<CategoryJSON> fetchByCategoryNamesByType(String type){
        List<CategoryJSON> catNames = new ArrayList<>();
        if(type.equals("expenses"))
            catNames = this.categoryRepository.findByCategoryType(CategoryType.EXPENSE)
                    .stream().filter(categoryEntity -> categoryEntity.getCategoryType() == CategoryType.EXPENSE &&categoryEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid())
                    .map(cat -> new CategoryJSON(cat.getCid(), cat.getCategoryName())).collect(Collectors.toList());
        else if(type.equals("incomes"))
            catNames = this.categoryRepository.findByCategoryType(CategoryType.INCOME)
                    .stream().filter(categoryEntity -> categoryEntity.getCategoryType() == CategoryType.INCOME && categoryEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid())
                    .map(cat -> new CategoryJSON(cat.getCid(), cat.getCategoryName())).collect(Collectors.toList());
         return catNames;
     }


    public Category createCategory(CategoryManipulationRequest categoryRequest) {
        System.out.println(this.userService.getLoggedInUserEntity().getUid());
       CategoryEntity savedCategory = this.categoryRepository.save( new CategoryEntity(userService.getLoggedInUserEntity(),categoryRequest.getCategoryName(), CategoryType.valueOf(categoryRequest.getCategoryType())));

        if (savedCategory.getCid() > 0) return mapToCategory(savedCategory);
        else return null;
    }


    private Category mapToCategory(CategoryEntity categoryEntity){
        List<Long> expensesIds = categoryEntity.getExpenses().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        List<Long> incomesIds = categoryEntity.getIncomes().stream().filter(expenseEntity -> expenseEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        return new Category( categoryEntity.getCid(), this.userService.getLoggedInUser().getUid(),categoryEntity.getCategoryName(), categoryEntity.getCategoryType().name(),expensesIds, incomesIds);
    }

    private Optional<CategoryEntity> mapToCategoryEntity(CategoryManipulationRequest categoryReq){
        Optional<CategoryEntity> categoryByName = this.categoryRepository.findByCategoryName(categoryReq.getCategoryName());
        if(categoryByName.isPresent()){
            return categoryByName;
        }else{
            throw  new ResourceNotFound("No such Category in DB");
        }

    }
}
