package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.CategoryManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.model.Expense;
import de.htwberlin.webtech.expensetracker.web.model.ExpenseManipulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

     public Category fetchById(Long cid){
         Optional<CategoryEntity> catById = this.categoryRepository.findById(cid);
         if(catById.isPresent()) return mapToCategory(catById.get());
         else return null;
     }


    public Category createCategory(CategoryManipulationRequest categoryRequest) {
        CategoryEntity categoryEntity = this.mapToCategoryEntity(categoryRequest) ;
        CategoryEntity savedCategory = this.categoryRepository.save(categoryEntity);
        System.out.println(savedCategory.getCid());
        System.out.println( mapToCategory(savedCategory).getCid());
        if (savedCategory != null && savedCategory.getCid() > 0) return mapToCategory(savedCategory);
        else return null;
    }

    private Category mapToCategory(CategoryEntity categoryEntity){
        return new Category(categoryEntity.getCid() ,categoryEntity.getCategoryName());
    }

    private CategoryEntity mapToCategoryEntity(CategoryManipulationRequest categoryReq){
        return new CategoryEntity(categoryReq.getCategoryName());
    }
}
