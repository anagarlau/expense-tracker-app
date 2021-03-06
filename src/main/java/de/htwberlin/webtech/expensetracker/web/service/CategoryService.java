package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.persistence.repository.CategoryRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.TransactionRepository;
import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.CategoryJSON;
import de.htwberlin.webtech.expensetracker.web.model.CategoryManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    private TransactionService transactionService;
    private UserService userService;
    private TransactionRepository transactionRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, TransactionService transactionService, UserService userService, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionService = transactionService;
        this.userService = userService;
        this.transactionRepository=transactionRepository;
    }


     public List<CategoryJSON> fetchCategoryNamesPerTypeAndUser(String type){
        List<CategoryJSON> catNames = new ArrayList<>();
        if(type.equals("expenses"))
            catNames = this.categoryRepository.findByCategoryTypeAndUserUid(CategoryType.EXPENSE, userService.getLoggedInUser().getUid())
                    .stream().map(cat -> new CategoryJSON(cat.getCid(), cat.getCategoryName(), cat.getCategoryType().name(), cat.getIcon())).collect(Collectors.toList());
        else if(type.equals("incomes"))
            catNames =  this.categoryRepository.findByCategoryTypeAndUserUid(CategoryType.INCOME, userService.getLoggedInUser().getUid())
                    .stream()
                    .map(cat -> new CategoryJSON(cat.getCid(), cat.getCategoryName(), cat.getCategoryType().name(), cat.getIcon())).collect(Collectors.toList());
        else if(type.equals("all"))
            catNames=this.categoryRepository.findByUserUid(userService.getLoggedInUser().getUid())
                    .stream()
                    .map(cat-> new CategoryJSON(cat.getCid(), cat.getCategoryName(), cat.getCategoryType().name(), cat.getIcon()))
                    .collect(Collectors.toList());
         return catNames;
     }



//

    public Category createCategory(CategoryManipulationRequest categoryRequest) {
        List<CategoryEntity> byUserUid = this.categoryRepository.findByUserUid(this.userService.getLoggedInUser().getUid());
        List<CategoryEntity> duplicates = byUserUid.stream().filter(categoryEntity -> categoryEntity.getCategoryName().toLowerCase().equals(categoryRequest.getCategoryName().toLowerCase())).collect(Collectors.toList());
        if(duplicates.isEmpty()){
            CategoryEntity savedCategory = this.categoryRepository.save( new CategoryEntity(userService.getLoggedInUserEntity(),categoryRequest.getCategoryName(),CategoryType.valueOf(categoryRequest.getCategoryType()), categoryRequest.getIcon()));
            if (savedCategory.getCid() > 0) return mapToCategory(savedCategory);
            else return null;
        }

        else return null;
    }

    public boolean deleteCategory(Long cid){
        Optional<CategoryEntity> byCidAndUserUid = this.categoryRepository.findByCidAndUserUid(cid, userService.getLoggedInUserEntity().getUid());
        if(byCidAndUserUid.isEmpty()){
            throw new ResourceNotFound("Category " + cid + " does not exist in DB");
        }else{
            this.transactionRepository.deleteAll(byCidAndUserUid.get().getTransactions());
            this.categoryRepository.deleteById(cid);
            return true;
        }
    }


    private Category mapToCategory(CategoryEntity categoryEntity){
        List<Long> transactionIds = categoryEntity.getTransactions().stream().filter(transactionEntity -> transactionEntity.getUser().getUid() == this.userService.getLoggedInUser().getUid()).map(expenseEntity -> expenseEntity.getId()).collect(Collectors.toList());
        return new Category( categoryEntity.getCid(), this.userService.getLoggedInUser().getUid(),categoryEntity.getCategoryName(), categoryEntity.getCategoryType().name(),transactionIds, categoryEntity.getIcon());
    }


}
