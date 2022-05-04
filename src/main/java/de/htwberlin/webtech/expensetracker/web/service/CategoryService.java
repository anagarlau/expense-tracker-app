package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.CategoryManipulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    private ExpenseService expenseService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ExpenseService expenseService) {
        this.categoryRepository = categoryRepository;
        this.expenseService = expenseService;
    }

    public List<Category> fetchAllCategories(){
        return this.categoryRepository.findAll().stream().map(categoryEntity -> this.mapToCategory(categoryEntity)).collect(Collectors.toList());
     }

     public List<String> fetchByCategoryNamesByType(String type){
        List<String> catNames = new ArrayList<>();
        if(type.equals("expenses"))
            catNames = this.categoryRepository.findByCategoryType(CategoryType.EXPENSE)
                    .stream().filter(categoryEntity -> categoryEntity.getCategoryType() == CategoryType.EXPENSE).map(cat -> cat.getCategoryName()).collect(Collectors.toList());
        else if(type.equals("incomes"))
            catNames = this.categoryRepository.findByCategoryType(CategoryType.INCOME)
                    .stream().filter(categoryEntity -> categoryEntity.getCategoryType() == CategoryType.EXPENSE).map(cat -> cat.getCategoryName()).collect(Collectors.toList());
         return catNames;
     }


    public Category createCategory(CategoryManipulationRequest categoryRequest) {
       // CategoryEntity categoryEntity = this.mapToCategoryEntity(categoryRequest) ;
        CategoryEntity savedCategory = this.categoryRepository.save(new CategoryEntity(categoryRequest.getCategoryName(), CategoryType.valueOf(categoryRequest.getCategoryType())));

        if (savedCategory.getCid() > 0) return mapToCategory(savedCategory);
        else return null;
    }

    private Category mapToCategory(CategoryEntity categoryEntity){
        List<Long> expensesIds = categoryEntity.getExpenses().stream().map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        return new Category(categoryEntity.getCid(), categoryEntity.getCategoryName(), categoryEntity.getCategoryType().name(),expensesIds);
    }

    private CategoryEntity mapToCategoryEntity(CategoryManipulationRequest categoryReq){
        CategoryEntity categoryByName = this.categoryRepository.findByCategoryName(categoryReq.getCategoryName());
        if(categoryByName != null){
            return categoryByName;
        }else{
            throw  new ResourceNotFound("No such Category in DB");
        }

    }
}
